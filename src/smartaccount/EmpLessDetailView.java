/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartaccount;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ANKIT
 */
public class EmpLessDetailView {
    
    private final SimpleStringProperty empid;
    private final SimpleStringProperty empname;
    private final SimpleStringProperty state;
    private final SimpleStringProperty city;
    
    
    public EmpLessDetailView(String empids, String empnames, String states, String citys){
            this.empid = new SimpleStringProperty(empids);
            this.empname = new SimpleStringProperty(empnames);
            this.state = new SimpleStringProperty(states);
            this.city = new SimpleStringProperty(citys);
    }
    //getter
    public String getEmpid(){return empid.get();}
    public String getEmpname(){return empname.get();}
    public String getState(){return state.get();}
    public String getCity(){return city.get();}
    //setter
    public void setEmpid(String values){empid.set(values);}
    public void setEmpname(String values){empname.set(values);}
    public void setState(String values){state.set(values);}
    public void setCity(String values){city.set(values);}
      
}
