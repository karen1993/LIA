/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.HashMap;


/**
 *
 * @author PC
 */
public class LIA 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
     
       File archivo = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia\\setDatosNoEtiquetados1.arff");
       FileWriter crear = new FileWriter(archivo);
       FileReader json = new FileReader("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia\\dataset_cie.json");
       BufferedReader aux = new BufferedReader(json);
        
       //crear.write("holamundo;como estas;\r\n1;2");
        crear.write("@relation setDatos\r\n\r\n");
        crear.write("@attribute nombre STRING\r\n");
        crear.write("@attribute sexow {femenino, masculino, otro}\r\n");
        crear.write("@attribute tipo_prestacion_term STRING\r\n");
        crear.write("@attribute tag STRING\r\n");
        crear.write("@attribute automatico STRING\r\n");
        crear.write("@attribute auditado STRING\r\n");
        crear.write("@attribute concepto_corregido{si,no}\r\n\r\n");
        crear.write("@data\r\n");
        leer(aux,crear);
        crear.close();
        aux.close();
    }
    public static void leer(BufferedReader aux, FileWriter crear) throws IOException
    {
        String cadena,aux2="";
        int positivo = 0, negativo =0;
         int pos;
         int intervalo =0;
        while((cadena = aux.readLine()) != null)
        {
            String id="nada,",t_id="nada,",sexo="nada,",edad="nada,",tipo_P="nada,",p_id="nada,",
                estado="nada,",c_id="nada,",
                tag="nada,",auto="nada,", auditado ="nada,",principal="nada,", nombre="nada,";
       
            cadena= cadena.substring(1,cadena.length()-1);
            String[] datos = cadena.split(",");
          //  auditado="";
            for (int i = 0; i < datos.length; i++) 
            {
                pos = datos[i].indexOf(":");
               // System.out.println(pos);
                if(pos>0)
                aux2 = datos[i].substring(1,pos-1);
             //   System.out.println(aux2);
             ///pos=1;
                switch (aux2)
                {
                    case "id":
                        id=datos[i].substring(pos+1)+",";
                    break;
                    case "estado_agenda":
                        estado=datos[i].substring(pos+1)+",";
                    break;
                    case "id_turno":
                        t_id=datos[i].substring(pos+1)+",";
                    break;
                    case "sexo":
                        sexo=(datos[i].substring(pos+1)).replace("\"", "")+",";
                       
                    break;
                    case "edad":
                        edad=datos[i].substring(pos+1);
                        if(edad.length()<4)
                            edad = edad.substring(0, edad.length()-1);
                        else
                        {
                            edad=edad.substring(0,4)+",";
                        }
                                //+";";
                    break;
                   
                    case "tipo_prestacion_conceptid":
                        p_id=datos[i].substring(pos+1)+",";
                    break;
                    case "tipo_prestacion_term":
                        tipo_P="\""+(datos[i].substring(pos+1)).replace("\"", "")+"\",";
                    break;
                    
                    case "concept_id":
                        c_id=datos[i].substring(pos+1)+",";
                    break;
                    case "concept_term":
                        nombre="\""+(datos[i].substring(pos+1)).replace("\"", "")+"\",";
                    break;
                    case "concept_semantictag":
                        tag="\""+(datos[i].substring(pos+1)).replace("\"", "")+"\",";
                    break;
                    case "cie10_automatico_codigo":
                        auto="\""+(datos[i].substring(pos+1)).replace("\"", "")+"\",";
                    break;
                    case "principal":
                        principal=datos[i].substring(pos+1)+",";
                    break;
                    case "cie10_auditado_codigo":
                        auditado = "\""+(datos[i].substring(pos+1)).replace("\"", "")+"\",";
                    break;    
                                
                        
                    
                }                
            }
         //  System.out.println(auditado);
         //faltan las comillas y los ;
       if(estado.equalsIgnoreCase("\"auditada\",") && !tag.equalsIgnoreCase("\"procedimiento\",")
               &&!nombre.equalsIgnoreCase("nada,")
               &&!sexo.equalsIgnoreCase("nada,") 
               &&!tipo_P.equalsIgnoreCase("nada,")
                &&!tag.equalsIgnoreCase("nada,"))
      
        
         {
           //  System.out.println(tag);
            //crear.write(auto);
            //crear.write(auditado);
           // crear.write(principal);
             //System.out.println(auditado);
             
             if(!auto.equalsIgnoreCase(auditado) && !auditado.equalsIgnoreCase("nada,") && positivo < 5000 )
             {
                if(intervalo > 10000)
                {
                    crear.write(cleanString((nombre).replace("ñ", "ni").replace("'", "")));
                    crear.write(cleanString((sexo).replace("ñ", "ni").replace("'", "")));
                    crear.write(cleanString((tipo_P).replace("ñ", "ni").replace("'", "")));
                    crear.write(cleanString((tag).replace("ñ", "ni").replace("'", "")));
                    crear.write(cleanString((auto).replace("ñ", "ni").replace("'", "")));
                    crear.write(cleanString((auditado).replace("ñ", "ni").replace("'", "")));
                    crear.write("?");
                    crear.write("\r\n");
                    positivo++;
                }
                intervalo++;
             }
             else
             {
        
                 if(negativo < 1000)
                { 
                    if(intervalo > 10000)
                    {
                        crear.write(cleanString(nombre).replace("ñ", "ni").replace("'", ""));
                        crear.write(cleanString(sexo).replace("ñ", "ni").replace("'", ""));
                         crear.write(cleanString(tipo_P).replace("ñ", "ni").replace("'", ""));
            
                        crear.write(cleanString(tag).replace("ñ", "ni").replace("'", ""));
                        crear.write(cleanString(auto).replace("ñ", "ni").replace("'", ""));
                        crear.write(cleanString(auditado).replace("ñ", "ni").replace("'", ""));
                        crear.write("?");
                        crear.write("\r\n");
                        negativo++;
                    }
                    intervalo++;
                }
             }    
            
         }   
        }
    }
    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }
    
}
/*
 pos= datos[i].indexOf(":");
                
                crear.write(datos[i].substring(pos+1)+";");
*/