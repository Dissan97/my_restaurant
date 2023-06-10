package org.dissan.restaurant.patterns.behavioral.observer;

import org.dissan.restaurant.beans.TableBean;
import org.dissan.restaurant.patterns.behavioral.observer.subjects.TableSubjectStates;

import java.util.Objects;


public class AttendantTableObserver extends TableObserver{
    public AttendantTableObserver(String subject) {
        super(subject);

    }

    @Override
    public void update(TableObserver observer, TableBean tableBean, TableSubjectStates state) {
        if (!(observer instanceof AttendantTableObserver) && (Objects.requireNonNull(state) == TableSubjectStates.PAY_REQUEST)) {
                tableBean.setValidPay(true);//Always good pay for now must be updated
        }
    }


}
