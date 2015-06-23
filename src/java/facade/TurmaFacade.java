/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.Turma;
import org.hibernate.SessionFactory;

@Stateless
public class TurmaFacade extends AbstractFacade<Turma> {

    @Override
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    public TurmaFacade() {
        super(Turma.class);
    }
}
