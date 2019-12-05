/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LIA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import static lia_aux.LIA.leer;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
/**
 *
 * @author PC
 */
public class Filtro implements Serializable
{
    private HashMap <String, Integer> dic;
    private HashMap <Integer, String> dicInv;
    private int cont;
    private Instances nuevasInstancias;
    
    public Filtro() throws IOException, Exception
    {
       dic = new HashMap();
       dicInv = new HashMap();
       dic.put("femenino", 0);
       dic.put("masculino",1);
       dic.put("otro",2);
      
       dicInv.put(0, "femenino");
       dicInv.put(1,"masculino");
       dicInv.put(2,"otro");
       nuevasInstancias =crearArff();
       cont = 3;
       
    }
    public Instances crearArff() throws IOException, Exception
    {
        File archivo = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\numerico.arff");
        FileWriter crear = new FileWriter(archivo);
      
        
       //crear.write("holamundo;como estas;\r\n1;2");
        crear.write("@relation setDatos\r\n\r\n");
        //crear.write("@attribute nombre Numeric\r\n");
        crear.write("@attribute sexow Numeric\r\n");
        crear.write("@attribute tipo_prestacion_term Numeric\r\n");
        crear.write("@attribute tag Numeric\r\n");
        crear.write("@attribute automatico Numeric\r\n");
//        crear.write("@attribute auditado STRING\r\n");
        crear.write("@attribute concepto_corregido {si,no}\r\n\r\n");
        crear.write("@data\r\n");
        
        crear.close();
        
         ConverterUtils.DataSource s5 = new ConverterUtils.DataSource("C:\\Users\\PC\\Documents\\NetBeansProjects"
                 + "\\LIA\\src\\lia_aux\\numerico.arff");
            
            
            nuevasInstancias = s5.getDataSet();
             nuevasInstancias.setClassIndex(nuevasInstancias.numAttributes()-1);
        return nuevasInstancias; 
    }
    
    public Instances aplicarFiltro(Instances instancias)
    {
        // nuevasInstancias = new Instances(instancias,0,1);
        //nuevasInstancias.delete();
        
        Instance ins;
        int j = 0;
        String cad;
        double [] arreglo ;
        for (int i = 0; i < instancias.size(); i++) 
        {
           arreglo = new double[instancias.get(0).numAttributes()];
            ins = instancias.get(i);
        //    arreglo = new double [ins.numAttributes()];
            for ( j = 0; j < ins.numAttributes()-1; j++) 
            {
                cad = ins.toString(j);
               
                if(!dic.containsKey(cad))
                {
             //       System.out.println(cont);
                    dic.put(cad, cont);
                    dicInv.put(cont,cad);
                    arreglo[j]=cont;
                    cont ++;
                  
                }
                else
                {
                    arreglo[j]=dic.get(cad);
                    
                }
            }
            DenseInstance p = new DenseInstance(1.0, arreglo);
            if(ins.toString(ins.numAttributes()-1).equalsIgnoreCase("si"))
            {
                p.setValue(ins.numAttributes()-1, 0.0);
            }    
            else
            {
                 p.setValue(ins.numAttributes()-1, 1.0);
            }
//            System.out.println(p.value(1));
           
            nuevasInstancias.add(p);
            
        }
        
//        System.out.println(nuevasInstancias.toString());
        return nuevasInstancias;
    }

    
    public void convertir_CVS(Instances instancias, String nom) throws IOException
    {
        File archivo = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\"
                + "LIA\\src\\lia_aux\\"+nom+".csv");
        
        FileWriter crear = new FileWriter(archivo);
        String cad ="",aux2 ="";
        int aux;
        Instance ins;
        for (int i = 0; i < instancias.size(); i++) 
        {
            ins = instancias.get(i);
            for (int j = 0; j < ins.numAttributes()-1; j++) 
            {
                aux = Integer.parseInt(ins.toString(j));
               // System.out.println(aux);
                if(dicInv.containsKey(aux))
                {
                    aux2 = dicInv.get(aux);
                    cad += aux2+",";
                }
                else
                {
                    System.out.println("ERROR EL VALOR NO ESTA EN EL DICCIONARIO "+ aux);
                    System.exit(0);
                }
                
            }
            cad += ins.toString(ins.numAttributes()-1)+"\r\n";
            crear.write(cad);
            cad ="";
        }
        crear.close();
    }
    
     public void convertir_Arff(Instances instancias, String nom) throws IOException
    {
        File archivo = new File(nom);
        
        FileWriter crear = new FileWriter(archivo);
        String cad ="",aux2 ="";
        int aux;
        crear.write("@relation setDatos\r\n\r\n");
        crear.write("@attribute sexow {femenino, masculino, otro}\r\n");
        crear.write("@attribute tipo_prestacion_term STRING\r\n");
        crear.write("@attribute tag STRING\r\n");
        crear.write("@attribute automatico STRING\r\n");
        crear.write("@attribute concepto_corregido{si,no}\r\n\r\n");
        crear.write("@data\r\n");
        Instance ins;
        for (int i = 0; i < instancias.size(); i++) 
        {
            ins = instancias.get(i);
            for (int j = 0; j < ins.numAttributes()-1; j++) 
            {
                aux = Integer.parseInt(ins.toString(j));
               // System.out.println(aux);
                if(dicInv.containsKey(aux))
                {
                    aux2 = dicInv.get(aux);
                    cad += aux2+",";
                }
                else
                {
                    System.out.println("ERROR EL VALOR NO ESTA EN EL DICCIONARIO "+ aux);
                    System.exit(0);
                }
                
            }
            cad += ins.toString(ins.numAttributes()-1)+"\r\n";
            crear.write(cad);
            cad ="";
        }
        crear.close();
    }
    
    
}
