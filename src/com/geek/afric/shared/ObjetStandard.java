package com.geek.afric.shared;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * 
 * @author Mcicheick
 *
 */

@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class ObjetStandard implements Serializable{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	protected String id;
	@Persistent
	protected Date dateCreation=new Date();
	@Persistent
	protected Date dateDerniereModif = new Date();
	public ObjetStandard() {
		super();	
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateDerniereModif() {
		return dateDerniereModif;
	}

	public void setDateDerniereModif(Date dateDerniereModif) {
		this.dateDerniereModif = dateDerniereModif;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime
				* result
				+ ((dateDerniereModif == null) ? 0 : dateDerniereModif
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ObjetStandard)) {
			return false;
		}
		ObjetStandard other = (ObjetStandard) obj;
		if (dateCreation == null) {
			if (other.dateCreation != null) {
				return false;
			}
		} else if (!dateCreation.equals(other.dateCreation)) {
			return false;
		}
		if (dateDerniereModif == null) {
			if (other.dateDerniereModif != null) {
				return false;
			}
		} else if (!dateDerniereModif.equals(other.dateDerniereModif)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
