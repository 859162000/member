package com.wanda.ccs.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractStateUVEntity extends
		ApprovableUVEntity {

	private static final long serialVersionUID = 4955299217477001848L;

	@Transient
	public State getState() {
		return State.fromOrdinal(getStatus());
	}

	public void setState(State state) {
		super.setStatus("" + state.ordinal());
	}

}
