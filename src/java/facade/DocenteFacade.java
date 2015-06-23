/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.Docente;
import org.hibernate.SessionFactory;

@Stateless
public class DocenteFacade extends AbstractFacade<Docente> {

    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    public DocenteFacade() {
        super(Docente.class);
    }
}

