package com.xcesys.extras.webapp.listener;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultSaveOrUpdateEventListener;

import com.xcesys.extras.core.dao.model.AbstractEntity;
import com.xcesys.extras.core.dao.model.BlameableEntity;
import com.xcesys.extras.core.dao.model.DeleteableEntity;
import com.xcesys.extras.core.dao.model.TimestampableEntity;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.util.SecurityUtil;

public class ConHibernateOnSaveorUpdateEventListener extends
		DefaultSaveOrUpdateEventListener {

	private static final long serialVersionUID = 5900840195660163032L;
	protected final Log log = LogFactory.getLog(getClass());

	public ConHibernateOnSaveorUpdateEventListener()
	{
	}

	public void onSaveOrUpdate(SaveOrUpdateEvent event)
		throws HibernateException
	{
		
		if(event.getEntry() != null)
			event.getEntry().requiresDirtyCheck(event.getEntity());
		
		
		Object object = event.getObject();
		Date currentDate = DateUtil.getCurrentDate();
		if (object instanceof AbstractEntity)
		{
			AbstractEntity entity = (AbstractEntity)object;
			Long id = entity.getId();
			if (id != null && id.longValue() <= 0L)
				entity.setId(null);
			if (object instanceof TimestampableEntity)
			{
				TimestampableEntity ent = (TimestampableEntity)entity;
				if (entity.getId() == null)
					ent.setCreatedDate(currentDate);
				ent.setUpdatedDate(currentDate);
				log.debug("Update 'createdDate & updatedDate' while flushing entity.");
			}
			if (object instanceof BlameableEntity)
			{
				String loginUser = SecurityUtil.getLoginUser();
				if (entity.getId() == null)
					((BlameableEntity)entity).setCreatedBy(loginUser);
				((BlameableEntity)entity).setUpdatedBy(loginUser);
				log.debug("Update 'createdBy& updatedBy' while flushing entity.");
			}
			if (object instanceof DeleteableEntity)
			{
				String loginUser = SecurityUtil.getLoginUser();
				if (((DeleteableEntity)entity).getDeleted() != null && ((DeleteableEntity)entity).getDeleted().booleanValue())
				{
					((DeleteableEntity)entity).setDeletedDate(currentDate);
					((DeleteableEntity)entity).setDeletedBy(loginUser);
					log.debug("Update 'deletedDate & DeletedBy' while flushing entity.");
				}
			}
		}
		super.onSaveOrUpdate(event);
	}
	
}
