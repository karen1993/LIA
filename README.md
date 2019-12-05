# LIA
Entrenamiento semisupervisado

====================
Manual de Uso
====================


LIA es el encargado de generar los archivos arff de datos etiquetados y no etiquetados, para ello se debe:

1. Modificar la ruta de las variables archivo, archivo2 (donde se quieran guardar), json  y json2(la ubicacion del dataset)

2. Para ejecutar el algoritmo cotraining  hay que ejecutar Main_Cotraining:
	se deben modificar los 3 parametros con las rutas donde se encuentran guardados los archivos arff creados anteriormente
	y la ubicacion de auxiliar.arff	el cual se encuentra en el paquete enviado

3. En la clase Co_Training modificar:
	la ruta de la variable nuevo

	En las siguientes sentencias cambiar la ruta 


	SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia\\bayes"), fc);
    SerializationHelper.write(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia\\J48"), fc2);
	
	fil = (Filtro)SerializationHelper.read(new FileInputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\FilManual"));


	fc = (FilteredClassifier)SerializationHelper.read(new FileInputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\bayes"))

	fc2 = (FilteredClassifier)SerializationHelper.read(new FileInputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\j48"));

	 ObjectOutputStream objFiltro = new ObjectOutputStream(new FileOutputStream("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\FilManual"));


4. En la clase Filtro modificar:
	
	File archivo = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\numerico.arff");

	ConverterUtils.DataSource s5 = new ConverterUtils.DataSource("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\numerico.arff");

	File archivo = new File("C:\\Users\\PC\\Documents\\NetBeansProjects\\LIA\\src\\lia_aux\\"+nom+".csv");
