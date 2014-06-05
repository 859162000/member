package com.wanda.ccs.model;

import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.ApprovableEntity;

public abstract class ConAbstractStateEntity extends ApprovableEntity {

	private static final long serialVersionUID = -3012576641834197735L;
	
	public abstract String getEntityName();
	
	/**
	 * 获得主干对象
	 * @return
	 */
	@Transient
	public ConAbstractStateEntity getTrunk(){
		return null;
	}
	

	@Transient
	public EnumConState getState() {
		return EnumConState.fromOrdinal(getStatus());
	}

	public void setState(EnumConState state) {
		super.setStatus("" + state.ordinal());
		if(state == EnumConState.SUBMIT || state == EnumConState.APPROVED || state == EnumConState.REJECTED){
			super.setSubmit(true);
		}else{
			super.setSubmit(false);
		}
	}
}
