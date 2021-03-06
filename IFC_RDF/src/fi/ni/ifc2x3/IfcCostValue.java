package fi.ni.ifc2x3;
import fi.ni.ifc2x3.interfaces.*;
import fi.ni.*;
import java.util.*;

/*
 * IFC Java class
 * @author Jyrki Oraskari
 * @license This work is licensed under a Creative Commons Attribution 3.0 Unported License.
 * http://creativecommons.org/licenses/by/3.0/ 
 */

public class IfcCostValue extends IfcAppliedValue implements IfcMetricValueSelect
{
 // The property attributes
 String costType;
 String condition;


 // Getters and setters of properties

 public String getCostType() {
   return costType;
 }
 public void setCostType(String value){
   this.costType=value;

 }

 public String getCondition() {
   return condition;
 }
 public void setCondition(String value){
   this.condition=value;

 }

}
