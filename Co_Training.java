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
        try {
            
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(nomArchivo);
       //     System.out.println( source.getStructure());
            ConverterUtils.DataSource auxi = new ConverterUtils.DataSource(nomAux);
            Instances auxiliar = source.getDataSet();
            auxiliar.setClassIndex(auxiliar.numAttributes()-1);
            
            Instances instances = source.getDataSet();
            instances.setClassIndex(instances.numAttributes()-1);
           ConverterUtils.DataSource source2 = new ConverterUtils.DataSource(nuevoArchivo);
            Instances instances2 = source2.getDataSet();
            instances2.setClassIndex(instances2.numAttributes()-1);

            
             //Clasificador A y B
           // Classifier aux3 = new NaiveBayes();
            //System.out.println(instances.toString());
            StringToWordVector filtro = new StringToWordVector(); 
            filtro.setInputFormat(instances);
            filtro.setIDFTransform(true);
            LovinsStemmer stemmer = new LovinsStemmer();
            filtro.setStemmer(stemmer);
            filtro.setLowerCaseTokens(true);
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(filtro);
           

//            SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia\\bayes"), fc);
//          
            FilteredClassifier bayes = (FilteredClassifier)SerializationHelper.read(new FileInputStream("C:\\Users"
                    + "\\XENIXS\\Documents\\NetBeansProjects\\LIA\\src\\lia\\bayes"));
                       
     /**       Classifier b = new J48();
            StringToWordVector filtro2 = new StringToWordVector(); 
            filtro2.setInputFormat(instances);
            filtro2.setIDFTransform(true);
            LovinsStemmer stemmer2 = new LovinsStemmer();
            filtro2.setStemmer(stemmer2);
            filtro2.setLowerCaseTokens(true);
            
            FilteredClassifier fc2 = new FilteredClassifier();
            fc2.setFilter(filtro2);
            fc2.setClassifier(a);
            fc2.buildClassifier(instances);
            
            SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\"
                    + "LIA\\src\\lia\\j48"), fc2);*/
            FilteredClassifier j48 = (FilteredClassifier)SerializationHelper.read(new FileInputStream("C:\\Users"
                    + "\\XENIXS\\Documents\\NetBeansProjects\\LIA\\src\\lia\\j48"));
           
            Evaluation evaluacionA = new Evaluation(instances);
            Evaluation evaluacionB = new Evaluation(instances);
            evaluacionA.evaluateModel(bayes, instances);
            evaluacionB.evaluateModel(j48, instances);
            double precisionBayes = (evaluacionA.correct()*100)/evaluacionA.numInstances();
            
           
            double precisionJ48 = (evaluacionB.correct()*100)/evaluacionB.numInstances();
            System.out.println(precisionJ48);
        
            System.out.println("CLASIFICADOR BAYESIANO");
            System.out.println(evaluacionA.toSummaryString());
            System.out.println(evaluacionA.toMatrixString());
            System.out.println("CLASIFICADOR J48");
            System.out.println(evaluacionB.toSummaryString());
            System.out.println(evaluacionB.toMatrixString());
            double result = -1;
            double result2 = -1;
            Instance inst;
            int intervalo = 0;
           // System.out.println("--------------------");
        //    this.imprimirAtributo(instances2.firstInstance());
            agregarInstancias(instances, auxiliar);
            
            for (int i = 0; i < 1000; i++) 
            {
             
             //   System.out.println(i+"---------------- ");
                inst = instances2.get(i);
                result = bayes.classifyInstance(inst);
                result2 = j48.classifyInstance(inst);
                
                if( result != -1 && result == result2)
                {
                    
                    inst.setValue(inst.attribute(6), result);
                    intervalo++;
                    
                        auxiliar.add(inst);
                    //    agregarInstancias(instances,auxiliar);
                        
                        bayes.buildClassifier(auxiliar);
                        j48.buildClassifier(auxiliar);
                    if(intervalo == 10)
                    {  
                        Evaluation a = new Evaluation(auxiliar); 
                        a.evaluateModel(bayes, auxiliar);
                        
                        Evaluation b = new Evaluation(auxiliar); 
                        b.evaluateModel(j48, auxiliar);
                        
                        double precisionB = (a.correct()*100)/a.numInstances();
                        double precisionJ = (b.correct()*100)/b.numInstances();
                        System.out.println("------------------ "+i+" -------------------------------");
                        System.out.println("Bayes: nueva "+precisionB+" anterior "+precisionBayes);
                        System.out.println("J48: nueva "+precisionJ+" anterior "+precisionJ48);
                        if(precisionBayes < precisionB && precisionJ48 < precisionJ)
                        {
                           // bayes = fc;
                            System.out.println("MEJOR PRECISION ");
                            precisionBayes = precisionB;
                            precisionJ48 = precisionJ;
                            instances.delete();
                            agregarInstancias(auxiliar,instances);
                          //  instances = auxiliar;
                        }
                        else
                        {
                            bayes.buildClassifier(instances);
                            j48.buildClassifier(instances);
                        }
                       
                        auxiliar.delete();
                        agregarInstancias(instances, auxiliar);
                        intervalo = 0;
                        
                        System.out.println("------------------------------");
                    }
                 }
                
            }
         
           aux.close();
           
            return instances;
        } catch (IOException e) 
            {
                System.out.println("Hay algo mal al leer el archivo " + e);
                return null;
            }
    }
    public void imprimirAtributo(Instance ins)
    {
       // for (int i = 0; i < 7; i++) 
        {
            
            System.out.println(ins);
            ins.setValue(ins.attribute(6),0.0);
            System.out.println(ins);
            
        }
    }
    public void agregarInstancias(Instances i1, Instances i2)
    {
        for (int i = 0; i < i1.size(); i++) 
        {
            i2.add(i1.get(i));
        }
    }
    
    public void leer(Instances instances, FileWriter crear) throws IOException
    {
        String cadena;
        int i = 0;
        while(instances != null && i < instances.size())
        {
            crear.write(instances.get(i).toString());
            crear.write("\n");
            i++;
        }
    }

   
    
}
