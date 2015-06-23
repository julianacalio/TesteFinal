/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.TurmaFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import model.Turma;
import util.TurmaDataModel;

@Named(value = "turmaController")
@SessionScoped
public class TurmaController implements Serializable {

    private Turma turma;

    @EJB
    private TurmaFacade turmaFacade;
    private TurmaDataModel turmaDataModel;

    public TurmaController() {
        turma = new Turma();

    }

    public Turma getTurma() {
        if (turma == null) {
            turma = new Turma();
        }
        return turma;
    }

    private Turma getTurma(Long key) {
        return this.find(key);

    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    //"Prepares" das paginas*********************************
    public String prepareView(){
        turma = (Turma) turmaDataModel.getRowData();
        return "/view/turma/View";
    }
    
    public String prepareCreate() {
        turma = new Turma();
        return "/view/turma/Create";

    }

    public String prepareEdit() {
        turma = (Turma) turmaDataModel.getRowData();
        return "/view/turma/Edit";
    }

    public String prepareList() {
        recriarModelo();
        return "/view/turma/List";
    }

    //DataModel**********************************************
    
    public TurmaDataModel getTurmaDataModel() {
        if (turmaDataModel == null) {
            List<Turma> turmas = this.findAll();
            turmaDataModel = new TurmaDataModel(turmas);
        }
        return turmaDataModel;
    }

    public void setTurmaDataModel(TurmaDataModel turmaDataModel) {
        this.turmaDataModel = turmaDataModel;
    }

    public void recriarModelo() {
        this.turmaDataModel = null;
    }

    //CRUD***************************************************
    
    public String create() {

        try {
            turmaFacade.save(turma);
            JsfUtil.addSuccessMessage("Turma " + turma.getID() + " criada com sucesso!");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia para salvar a turma");
            return null;
        }

    }

    public Turma find(Long id) {

        return turmaFacade.find(id);
    }

    private List<Turma> findAll() {
        return turmaFacade.findAll();

    }

    public void update() {
        try {
            turmaFacade.edit(turma);
            JsfUtil.addSuccessMessage("Turma editada com sucesso!");
            turma = null;

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Nao foi possivel editar, ocorreu o seguinte erro: " + e.getMessage());
        }

    }

    public void delete() {
        turma = (Turma) turmaDataModel.getRowData();
        try {
            turmaFacade.remove(turma);
            turma = null;
            JsfUtil.addSuccessMessage("Turma Deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia para deletar a turma");
        }

        recriarModelo();
    }

    //*******************************************************
    
    @FacesConverter(forClass = Turma.class)
    public static class TurmaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TurmaController controller = (TurmaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "turmaController");
            return controller.getTurma(getKey(value));
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
            if (object instanceof Turma) {
                Turma t = (Turma) object;
                return getStringKey(t.getID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Turma.class.getName());
            }
        }
    }

}
