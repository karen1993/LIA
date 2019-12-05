/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.LovinsStemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author PC
 */
public class Co_Training 
{
    
   public Instances open(String nomArchivo, String nuevoArchivo, String nomAux) throws Exception 
    {
        
        FileReader archivo = new FileReader(nomArchivo);
        BufferedReader aux = new BufferedReader(archivo);
        File nuevo = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\dic");
        FileWriter crear = new FileWriter(nuevo);
        int contPBayes=0, contNBayes=0, contPJ48=0, contNJ48=0,intervaloB=0,intervaloJ=0;
        try {
            
            ConverterUtils.DataSource s1 = new ConverterUtils.DataSource(nomArchivo);
            ConverterUtils.DataSource s2 = new ConverterUtils.DataSource(nomArchivo);
            ConverterUtils.DataSource s3 = new ConverterUtils.DataSource(nuevoArchivo);
            ConverterUtils.DataSource s6 = new ConverterUtils.DataSource(nuevoArchivo);
            ConverterUtils.DataSource s4 = new ConverterUtils.DataSource(nomAux);
            ConverterUtils.DataSource s5 = new ConverterUtils.DataSource(nomAux);
            
            
            Instances instancesBayes = s1.getDataSet();
            Instances instancesJ48 = s2.getDataSet();
            Instances instances2Bayes = s3.getDataSet();
            Instances instances2J48 = s6.getDataSet();
            Instances auxBayes = s4.getDataSet();
            Instances auxJ48 = s5.getDataSet();

            instancesBayes.setClassIndex(instancesBayes.numAttributes()-1);
            instancesJ48.setClassIndex(instancesJ48.numAttributes()-1);
            instances2Bayes.setClassIndex(instances2Bayes.numAttributes()-1);
            instances2J48.setClassIndex(instances2J48.numAttributes()-1);
            auxBayes.setClassIndex(auxBayes.numAttributes()-1);     
            auxJ48.setClassIndex(auxJ48.numAttributes()-1);
            Classifier a = new NaiveBayes();
            
            Filtro fil = new Filtro();
//            Filtro fil = (Filtro)SerializationHelper.read(new FileInputStream("C:\\Users"
//                    + "\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\FilManual"));;
            
           
            //leer el clasificador        
//            Filtro fil2 = 
            
            instancesBayes = fil.aplicarFiltro(instancesBayes);
            NumericToNominal filtro = new NumericToNominal(); 
            filtro.setInputFormat(instancesBayes);
           
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(filtro);
            fc.setClassifier(a);
            
            fc.buildClassifier(instancesBayes);
            //guardar el classificador
//            SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia\\bayes"), fc);
            //leer el clasificador        
//            FilteredClassifier bayes = (FilteredClassifier)SerializationHelper.read(new FileInputStream("C:\\Users"
//                    + "\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\bayes"));
//            
          //  System.out.println(filtro.getOutputFormat());
          //  nuevo = filtro.getDictionaryFileToSaveTo();
           // System.out.println(nuevo.toString());
            Classifier b = new J48();
            instancesJ48 = fil.aplicarFiltro(instancesJ48);
            NumericToNominal filtro2 = new NumericToNominal(); 
            filtro2.setInputFormat(instancesJ48);
            
            FilteredClassifier fc2 = new FilteredClassifier();
            fc2.setFilter(filtro2);
            fc2.setClassifier(b);
            
            fc2.buildClassifier(instancesJ48);
            
         
           // SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\"
           //         + "LIA\\src\\lia\\j48"), fc2);*/
//            FilteredClassifier j48 = (FilteredClassifier)SerializationHelper.read(new FileInputStream("C:\\Users"
//                    + "\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\j48"));
           
            Evaluation evaluacionA = new Evaluation(instancesBayes);
            Evaluation evaluacionB = new Evaluation(instancesJ48);
            evaluacionA.evaluateModel(fc, instancesBayes);
            evaluacionB.evaluateModel(fc2, instancesJ48);
            
            double precisionBayes = (evaluacionA.correct()*100)/evaluacionA.numInstances();
            double precisionJ48 = (evaluacionB.correct()*100)/evaluacionB.numInstances();
            
            System.out.println("CLASIFICADOR BAYESIANO");
            System.out.println(evaluacionA.toSummaryString());
            System.out.println(evaluacionA.toMatrixString());
            System.out.println("CLASIFICADOR J48");
            System.out.println(evaluacionB.toSummaryString());
            System.out.println(evaluacionB.toMatrixString());
            
            double result = -1;
            double result2 = -1;
            Instance instB, instJ;
            //tamIntervalo debe ser multiplo de dos 
            int tamIntervalo = 200;
     
           auxBayes = agregarInstancias(instancesBayes, auxBayes);

            
            auxJ48 = agregarInstancias(instancesJ48, auxJ48);
            instances2Bayes = fil.aplicarFiltro(instances2Bayes);
            
            for (int i = 0; i < instances2Bayes.size() && i < instances2J48.size() ; i++) 
            {
             
              
                instB = instances2Bayes.get(i);
                instJ = instances2J48.get(i);
                result = fc.classifyInstance(instB);
                result2 = fc2.classifyInstance(instJ);
                if( result != -1 )
                {
                    instB.setValue(instB.attribute(instB.numAttributes()-1), result);
                    if(result == 1.0 && contPBayes <= (int)tamIntervalo/2)
                    {
                        auxBayes.add((Instance) instB.copy());
                        contPBayes++;
                        intervaloB++;
                    }
                    else
                    {
                        if(result == 0.0 && contNBayes <= (int)tamIntervalo/2)
                        {
                            auxBayes.add((Instance) instB.copy());
                            contNBayes++;
                            intervaloB++;
                        }
                    }
                    if(intervaloB == tamIntervalo  || i == instances2Bayes.size()-1 )
                    {    
                        fc.buildClassifier(auxBayes);
                        Evaluation ai = new Evaluation(auxBayes); 
                        ai.evaluateModel(fc, auxBayes);
                        double precisionB = (ai.correct()*100)/ai.numInstances();
                        System.out.println("Bayes: nueva "+precisionB+" anterior "+precisionBayes);
                        if(precisionBayes < precisionB )
                        {
                            System.out.println(" Bayes MEJOR PRECISION ");
                            precisionBayes = precisionB;
                            instancesBayes = agregarInstancias(auxBayes,instancesBayes);
                        }
                        else
                        {
                            System.out.println("Bayes NO MEJORO");
                            fc.buildClassifier(instancesBayes);
                            
                        }
                        auxBayes = agregarInstancias(instancesBayes, auxBayes);
                        intervaloB = 0;
                        contPBayes =0;
                        contNBayes =0;
                    }
                }
                if(result2 != -1)
                {
                    instJ.setValue(instJ.attribute(instJ.numAttributes()-1), result2);
                       if(result2 == 1.0 && contPJ48 <= 100)
                        {
                            auxJ48.add(new DenseInstance(instJ));
                            contPJ48++;
                            intervaloJ++;
                        }
                        else
                        {
                            if(result2 == 0.0 && contNJ48 <= 100)
                            {
                                auxJ48.add(new DenseInstance(instJ));
                                contNJ48++;
                                intervaloJ++;
                            }
                        }
                    
                    if(intervaloJ == 200 || i == instances2J48.size()-1)
                    {
                        fc2.buildClassifier(auxJ48);
                        Evaluation bi = new Evaluation(auxJ48); 
                        bi.evaluateModel(fc2, auxJ48);
                        double precisionJ = (bi.correct()*100)/bi.numInstances();
                        System.out.println("J48: nueva "+precisionJ+" anterior "+precisionJ48);
                        if(precisionJ48 < precisionJ )
                        {
                            System.out.println(" J48 MEJOR PRECISION ");
                            precisionJ48 = precisionJ;
                            instancesJ48 = agregarInstancias(auxJ48,instancesJ48);
                        }
                        else
                        {
                            System.out.println("J48 NO MEJORO");
                            fc2.buildClassifier(instancesJ48);
                             
                        }
                        auxJ48 = agregarInstancias(instancesJ48, auxJ48);
                        contPJ48=0;
                        contNJ48=0;
                        intervaloJ = 0;
                    } 
                }
              
                
            }
            SerializationHelper.write(new FileOutputStream("C:\\Users\\PC"
                    + "\\Documents\\NetBeansProjects\\LIA\\src\\lia\\bayes"), fc);
            SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects"
                    + "\\LIA\\src\\lia\\J48"), fc2);
            fil.convertir_CVS(auxBayes,"bayes");
            fil.convertir_CVS(auxJ48,"J48");
            if(precisionBayes < precisionJ48)
            {
                fil.convertir_Arff(instancesJ48, nomArchivo);
            }
            else
            {
                fil.convertir_Arff(instancesBayes, nomArchivo);
            }
            //leer(auxBayes,crear);
           // leer(instances, crear);
//           ObjectOutputStream objFiltro = new ObjectOutputStream(new FileOutputStream("C:\\Users\\PC\\Documents\\"
//                   + "NetBeansProjects\\LIA\\src\\lia_aux\\FilManual"));
//           objFiltro.writeObject(fil);
           crear.close();
           aux.close();
//           objFiltro.close();
            return instancesBayes;
        } catch (IOException e) 
            {
                System.out.println("Hay algo mal al leer el archivo " + e);
                return null;
            }
    }
    public void imprimirAtributo(Instance ins)
    {
        for (int i = 0; i < 6; i++) 
        {
            
            System.out.println(ins);
            ins.setValue(ins.attribute(6),0.0);
            System.out.println(ins);
            
        }
    }
    public  void imprimir(Instances i)
    {
        for (int j = 0; j < i.size(); j++) 
        {
            try{
               
                System.out.println(i.get(j));}
            catch(Exception e)
            {
                System.out.println("se rompio en: "+j);
            }
        }
    }
    public Instances agregarInstancias(Instances i1, Instances i2)
    {
   
        i2 = new Instances(i1,0,i1.size());
        return i2;

    }
    
    public void leer(Instances instances, FileWriter crear) throws IOException
    {
        String cadena;
        int i = 0;
        while(instances != null && i < instances.size()-1)
        {
            crear.write(instances.get(i).toString());
            crear.write("\n");
            i++;
        }
    }
    
    

   
    
}
