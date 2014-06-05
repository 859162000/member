package com.xcesys.extras.webapp.listener;

import org.hibernate.HibernateException;
import org.hibernate.event.FlushEntityEvent;
import org.hibernate.event.def.DefaultFlushEntityEventListener;

public class ConHibernateFlushEntityEventListener extends DefaultFlushEntityEventListener{

	private static final long serialVersionUID = -5679712030089566949L;

	@Override
	public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
		super.onFlushEntity(event);
	}
	
}
