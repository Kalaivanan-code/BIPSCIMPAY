package com.bornfire.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@Entity
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "UPDATECBSRESINCCREDIT", procedureName = "UPDATECBSRESINCCREDIT", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "sequenceUniqueID", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "instrID", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "tranAuditNumber", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cbsStatus", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cbsStatusError", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cimStatus", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cimStatusCode", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cimMessage", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "tranNoFromCbs", type = String.class)

		}) })

public class UpdateCBSIncProcedure {
	@Id
	private String procedureName;

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	
}
