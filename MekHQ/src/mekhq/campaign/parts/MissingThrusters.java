/*
 * MissingAvionics.java
 * 
 * Copyright (c) 2009 Jay Lawson <jaylawson39 at yahoo.com>. All rights reserved.
 * 
 * This file is part of MekHQ.
 * 
 * MekHQ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MekHQ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MekHQ.  If not, see <http://www.gnu.org/licenses/>.
 */

package mekhq.campaign.parts;

import java.io.PrintWriter;

import megamek.common.Aero;
import megamek.common.Entity;
import megamek.common.EquipmentType;
import mekhq.MekHqXmlUtil;
import mekhq.campaign.Campaign;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jay Lawson <jaylawson39 at yahoo.com>
 */
public class MissingThrusters extends MissingPart {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7402791453470647853L;
	private boolean isLeftThrusters = false;

	public MissingThrusters() {
    	this(0, null);
    }
    
    public MissingThrusters(int tonnage, Campaign c) {
    	this(tonnage, c, false);
    }
    
    public MissingThrusters(int tonnage, Campaign c, boolean left) {
    	super(0, c);
    	this.name = "Thrusters";
    	isLeftThrusters = left;
    }
    
    @Override 
	public int getBaseTime() {
		return 4800;
	}
	
	@Override
	public int getDifficulty() {
		return 1;
	}
    
	@Override
	public String checkFixable() {
		return null;
	}

	@Override
	public Part getNewPart() {
		return new Thrusters(getUnitTonnage(), campaign, isLeftThrusters);
	}

	@Override
	public boolean isAcceptableReplacement(Part part, boolean refit) {
		return part instanceof Thrusters;
	}

	@Override 
	public void fix() {
		Part replacement = findReplacement(false);
		if(null != replacement) {
			Part actualReplacement = replacement.clone();
			unit.addPart(actualReplacement);
			campaign.addPart(actualReplacement, 0);
			replacement.decrementQuantity();
			((Thrusters)actualReplacement).setLeftThrusters(isLeftThrusters);
			remove(false);
			//assign the replacement part to the unit			
			actualReplacement.updateConditionFromPart();
		}
	}

	@Override
	public double getTonnage() {
		return 0;
	}

	@Override
	public int getTechRating() {
		return EquipmentType.RATING_C;
	}

	@Override
	public int getAvailability(int era) {
		return EquipmentType.RATING_C;
	}

	@Override
	public void updateConditionFromPart() {
		if(null != unit && unit.getEntity() instanceof Aero) {
			if (isLeftThrusters) {
				((Aero)unit.getEntity()).setLeftThrustHits(4);
			} else {
				((Aero)unit.getEntity()).setRightThrustHits(4);
			}
		}
	}
    
	@Override
	public void writeToXml(PrintWriter pw1, int indent) {
		writeToXmlBegin(pw1, indent);
		pw1.println(MekHqXmlUtil.indentStr(indent+1)
				+"<isLeftThrusters>"
				+isLeftThrusters
				+"</isLeftThrusters>");
		writeToXmlEnd(pw1, indent);
	}
	
	@Override
	protected void loadFieldsFromXmlNode(Node wn) {
		NodeList nl = wn.getChildNodes();
		
		for (int x=0; x<nl.getLength(); x++) {
			Node wn2 = nl.item(x);
			
			if (wn2.getNodeName().equalsIgnoreCase("isLeftThrusters")) {
				isLeftThrusters = Boolean.parseBoolean(wn2.getTextContent());
			}
		}
	}
	
	public boolean isLeftThrusters() {
		return isLeftThrusters;
	}
	
	public void setLeftThrusters(boolean b) {
		isLeftThrusters = b;
	}

	@Override
	public String getLocationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocation() {
		return Entity.LOC_NONE;
	}
	
	@Override
	public int getIntroDate() {
		return EquipmentType.DATE_NONE;
	}

	@Override
	public int getExtinctDate() {
		return EquipmentType.DATE_NONE;
	}

	@Override
	public int getReIntroDate() {
		return EquipmentType.DATE_NONE;
	}
	
}