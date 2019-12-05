/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LIA;


/**
 *
 * @author PC
 */
public class Main_Cotraining
        
        // los procedimientos deberian ser quitados, deberian ser transtorno y hallasgos
        
{
    public static void main(String[] args) throws Exception
    {
       
         Co_Training w = new Co_Training();
        w.open(
                "C:\\Users\\XENIXS\\Documents\\NetBeansProjects\\LIA\\src\\LIA_Prueba\\setDatosEtiquetados.arff",
                "C:\\Users\\XENIXS\\Documents\\NetBeansProjects\\LIA\\src\\LIA_Prueba\\setDatosNoEtiquetados1.arff",
                "C:\\Users\\XENIXS\\Documents\\NetBeansProjects\\LIA\\src\\LIA_Prueba\\auxiliar.arff");

    }
}
