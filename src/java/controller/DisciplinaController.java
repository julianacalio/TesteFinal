/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.DisciplinaFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import model.Disciplina;
import util.DisciplinaDataModel;

@Named(value = "disciplinaController")
@SessionScoped
public class DisciplinaController implements Serializable {

    private Disciplina disciplina;

    @EJB
    DisciplinaFacade disciplinaFacade;
    private static DisciplinaDataModel disciplinaDataModel;

    public DisciplinaController() {
        this.disciplina = new Disciplina();
    }

    public Disciplina getDisciplina() {
        if (disciplina == null) {
            disciplina = new Disciplina();
        }
        return disciplina;
    }

    private Disciplina getDisciplina(Long key) {
        return this.find(key);
    }

    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(disciplinaFacade.findAll(), true);
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    //Data Model*********************************************
    
    public DisciplinaDataModel getDisciplinaDataModel() {
        if (disciplinaDataModel == null) {
            List<Disciplina> disciplinas = this.findAll();
            disciplinaDataModel = new DisciplinaDataModel(disciplinas);
        }
        return disciplinaDataModel;
    }

    public static void setDisciplinaDataModel(DisciplinaDataModel disciplinaDataModel) {
        DisciplinaController.disciplinaDataModel = disciplinaDataModel;
    }

    public void recriarModelo() {
        DisciplinaController.disciplinaDataModel = null;
    }

    //"Prepares" das paginas*********************************
    public String prepareView(){
        disciplina = (Disciplina) disciplinaDataModel.getRowData();
        return "/view/disciplina/View";
    }    
    
    public String prepareCreate() {
        disciplina = new Disciplina();
            return "/view/disciplina/Create";
    }

    public String prepareEdit() {
        disciplina = (Disciplina) disciplinaDataModel.getRowData();
        return "/view/disciplina/Edit";
    }
    
    public String prepareList(){
        recriarModelo();
        return "/view/disciplina/List";
    }

    //CRUD***************************************************
   
    public String create() {

        try {
            disciplinaFacade.save(disciplina);
            JsfUtil.addSuccessMessage("Disciplina " + disciplina.getNome() + " criada com sucesso!");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia para salvar a disciplina " + disciplina.getNome());
            return null;

        }

    }

    public Disciplina find(Long id) {

        return disciplinaFacade.find(id);

    }

    public List<Disciplina> findAll() {
        return disciplinaFacade.findAll();
    }
    
    public void update() {
        try {
            disciplinaFacade.edit(disciplina);
            JsfUtil.addSuccessMessage("Disciplina editada com sucesso!");
            disciplina = null;

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro: " + e.getMessage() + " para editar a disciplina " + disciplina.getNome());
        }

    }

    public void delete() {
        disciplina = (Disciplina) disciplinaDataModel.getRowData();
        try {
            disciplinaFacade.remove(disciplina);
            disciplina = null;
            JsfUtil.addSuccessMessage("Disciplina Deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia para deletar a disciplina " + disciplina.getNome());
        }
        recriarModelo();

    }

    //*******************************************************
    @FacesConverter(forClass = Disciplina.class)
    public static class DisciplinaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DisciplinaController controller = (DisciplinaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "disciplinaController");
            return controller.getDisciplina(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Disciplina) {
                Disciplina d = (Disciplina) object;
                return getStringKey(d.getID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Disciplina.class.getName());
            }
        }
    }

}
