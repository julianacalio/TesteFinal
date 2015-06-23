package controller;

import facade.DocenteFacade;
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
import model.Docente;
import model.Turma;
import util.DocenteDataModel;

@Named(value = "docenteController")
@SessionScoped
public class DocenteController implements Serializable {

    public DocenteController() {
        docente = new Docente();
    }

    //Guarda o docente atual
    private Docente docente;

    @EJB
    private DocenteFacade docenteFacade;
    private static DocenteDataModel docenteDataModel;

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    private Docente getDocente(Long key) {
        return this.find(key);

    }

    public Docente getDocente() {
        if (docente == null) {
            docente = new Docente();
        }
        return docente;
    }

    public List<Turma> getTurmas() {
        return docente.getTurmas();
    }

    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(docenteFacade.findAll(), true);
    }

    //Data Model*********************************************
    
    public DocenteDataModel getDocenteDataModel() {
        if (DocenteController.docenteDataModel == null) {
           
            List<Docente> docentes = this.findAll();
            DocenteController.docenteDataModel = new DocenteDataModel(docentes);
        }
        return DocenteController.docenteDataModel;
    }

    public static void setDocenteDataModel(DocenteDataModel docenteDataModel) {
        DocenteController.docenteDataModel = docenteDataModel;
    }

    public void recriarModelo() {
        DocenteController.docenteDataModel = null;
    }

    //"Prepares" das paginas*********************************
    public String prepareView(){
        docente = (Docente) docenteDataModel.getRowData();
        return "/view/docente/View";
    }
    
    public String prepareCreate() {
        docente = new Docente();
        return "/view/docente/Create";    
    }

    public String prepareEdit() {
        docente = (Docente) docenteDataModel.getRowData();
        return "/view/docente/Edit";
    }
    
    public String prepareList(){
        recriarModelo();
        return "/view/docente/List";
    }

    //CRUD***************************************************
    public String create() {

        try {
            docenteFacade.save(docente);
            JsfUtil.addSuccessMessage("Docente " + docente.getNome() + " criado com sucesso!");
            docenteDataModel = null;
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia para salvar o docente " + docente.getNome());
            return null;
        }
    }

    public Docente find(Long id) {

        return docenteFacade.find(id);
    }

    private List<Docente> findAll() {
        return docenteFacade.findAll();

    }

    public void update() {
        try {
            docenteFacade.edit(docente);
            JsfUtil.addSuccessMessage("Docente Editado com sucesso!");
            docente = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia, nao foi possivel editar o docente: " + e.getMessage());

        }
    }

    public void delete() {
        docente = (Docente) docenteDataModel.getRowData();
        try {
            docenteFacade.remove(docente);
            docente = null;
            JsfUtil.addSuccessMessage("Docente Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia: " + e.getMessage() + " para deletar o docente " + docente.getNome());
        }

        recriarModelo();
    }

    //*******************************************************
    
    @FacesConverter(forClass = Docente.class)
    public static class DocenteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DocenteController controller = (DocenteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "docenteController");
            return controller.getDocente(getKey(value));
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
            if (object instanceof Docente) {
                Docente d = (Docente) object;
                return getStringKey(d.getID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Docente.class.getName());
            }
        }
    }

}