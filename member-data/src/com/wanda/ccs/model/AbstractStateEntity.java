package com.wanda.ccs.model;

import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.ApprovableEntity;

public abstract class AbstractStateEntity extends ApprovableEntity {

	private static final long serialVersionUID = 8801887872293610049L;

	@Transient
	public State getState() {
		return State.fromOrdinal(getStatus());
	}

	public void setState(State state) {
		super.setStatus("" + state.ordinal());
	}
}
