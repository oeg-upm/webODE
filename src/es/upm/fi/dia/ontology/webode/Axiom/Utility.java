package es.upm.fi.dia.ontology.webode.Axiom;

import java.lang.System;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;


class Utility{

  private static Hashtable tabla_ambito;
  private static Stack pila_ambito;
  private static int num;

  private static final String errorMsg[] = {
    "Error: Unmatched end-of-comment punctuation.",
    "Error: Unmatched start-of-comment punctuation.",
    "Error: Unclosed string.",
    "Error: Illegal character."
    };

// Angel 15-10-02 -->
// The method assert has become a reserved function from JDK1.4
/*  public static void assert( boolean expr)
      { 
	if (!expr) {
	  throw (new Error("Error: Assertion failed."));
	}
      }*/
// <-- Angel
  
  
  public static final int E_ENDCOMMENT = 0; 
  public static final int E_STARTCOMMENT = 1; 
  public static final int E_UNCLOSEDSTR = 2; 
  public static final int E_UNMATCHED = 3; 

  public static void error( int code )
  {
  	System.out.println(errorMsg[code]);
  }

/**********************************************
  public static void pinta(CTree arbol){
	if (arbol.type==CNodeType.NODO_HOJA)
			System.out.println(arbol.node);
	else{
			System.out.println(arbol.node);
			pinta(arbol.left_tree);
			pinta(arbol.right_tree);	 
	}
  }
**************************************************/


static void pinta(CTree arbol){
	int contador,longitud;

	if (arbol.type==CNodeType.NODO_HOJA)
			System.out.println(arbol.node);
	else if (arbol.type==CNodeType.NODO_FORALL){
			System.out.print(arbol.node);
			System.out.println(arbol.lista.toString());
			pinta(arbol.left_tree);
			pinta(arbol.right_tree);	 

	}else if (arbol.type==CNodeType.NODO_EXISTS){
                  System.out.print(arbol.node);
			System.out.println(arbol.lista.toString());	
			pinta(arbol.left_tree);
			pinta(arbol.right_tree);	 


	}else{
			System.out.println(arbol.node);
			pinta(arbol.left_tree);
			pinta(arbol.right_tree);	 
	}
  }
  static String pinta_formula_aux(CTree arbol){
	int contador,longitud;

	if (arbol.type==CNodeType.NODO_HOJA)
			return " " + arbol.node + " ";
	else if (arbol.type==CNodeType.NODO_FORALL || arbol.type==CNodeType.NODO_EXISTS ){
			return " " + arbol.node + arbol.lista.toString() + "(" + pinta_formula_aux(arbol.left_tree) + ")";
	}else if (arbol.type==CNodeType.NODO_NOT){
			return " " + arbol.node + "(" + pinta_formula_aux(arbol.left_tree) + ")";
	}else if (arbol.type==CNodeType.NODO_OR || arbol.type==CNodeType.NODO_AND || arbol.type==CNodeType.NODO_DOBLEIMPLICA || arbol.type==CNodeType.NODO_IMPLICA){
			return "(" + pinta_formula_aux(arbol.left_tree) + " " + arbol.node + " " + pinta_formula_aux(arbol.right_tree)+ ")";
	}else{
			System.err.println("ERROR");
			return null;
	}
  }
  public static void pinta_formula(CTree arbol)
  {
		String aux;
		aux=pinta_formula_aux(arbol);
		aux=aux.replace('$',' ');
		aux=aux.replace('~',' ');
		System.out.println(aux);
	
  }

  private static void QuitarImplicaciones(CTree arbol)
  {
	CTree aux;

	if (arbol.type==CNodeType.NODO_IMPLICA)
	{
		QuitarImplicaciones(arbol.left_tree);
		QuitarImplicaciones(arbol.right_tree);
		aux=new CTree();
		aux.Add(arbol.left_tree,"not",CNodeType.NODO_NOT);	
		arbol.Add(aux,"or",CNodeType.NODO_OR,arbol.right_tree);
	}
	else if ((arbol.type==CNodeType.NODO_HOJA) || (arbol.right_tree==null))
		return;
	else{
		QuitarImplicaciones(arbol.left_tree);
		QuitarImplicaciones(arbol.right_tree);
	}
  }
  private static void QuitarDoblesImplicaciones(CTree arbol)
  {
	CTree aux_izq1,aux_izq2;
	CTree aux_der1,aux_der2;

	if (arbol.type==CNodeType.NODO_DOBLEIMPLICA)
	{
		QuitarDoblesImplicaciones(arbol.left_tree);
		QuitarDoblesImplicaciones(arbol.right_tree);

		aux_izq1=new CTree();
		aux_izq2=new CTree();
		aux_izq2.Add(arbol.left_tree,"not",CNodeType.NODO_NOT);	
		aux_izq1.Add(aux_izq2,"or",CNodeType.NODO_OR,arbol.right_tree);

		aux_der1=new CTree();
		aux_der2=new CTree();
		aux_der2.Add(arbol.right_tree,"not",CNodeType.NODO_NOT);	
		aux_der1.Add(arbol.left_tree,"or",CNodeType.NODO_OR,aux_der2);

		arbol.Add(aux_izq1,"and",CNodeType.NODO_AND,aux_der1);



	}
	else if ((arbol.type==CNodeType.NODO_HOJA) || (arbol.right_tree==null))
		return;
	else{
		QuitarDoblesImplicaciones(arbol.left_tree);
		QuitarDoblesImplicaciones(arbol.right_tree);
	}
  }
  private static boolean CambiarNegacionNegacion(CTree padre)
  {
	CTree hijo;
	boolean aux1,aux2;

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_NOT) && (padre.left_tree.type == CNodeType.NODO_NOT)){

		hijo=padre.left_tree;
		padre.Add(hijo.left_tree.left_tree,hijo.left_tree.node,hijo.left_tree.type,hijo.left_tree.right_tree);
		padre.prolog=hijo.left_tree.prolog;
		CambiarNegacionNegacion(padre);
		return true;
	}else{
		aux1=CambiarNegacionNegacion(padre.left_tree);
		aux2=CambiarNegacionNegacion(padre.right_tree);
		return aux1 || aux2;
	}

  }
  private static boolean CambiarNegacionAnd(CTree padre)
  {
	CTree hijo,aux1,aux2;
	boolean aux3,aux4;	

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_NOT) && (padre.left_tree.type == CNodeType.NODO_AND)){

		hijo=padre.left_tree;
		
		aux1=new CTree();
		aux2=new CTree();
		aux1.Add(hijo.left_tree,"not",CNodeType.NODO_NOT);
		aux2.Add(hijo.right_tree,"not",CNodeType.NODO_NOT);
		padre.Add(aux1,"or",CNodeType.NODO_OR,aux2);
		CambiarNegacionAnd(padre);
		return true;

	}else{
		aux3=CambiarNegacionAnd(padre.left_tree) ;
		aux4=CambiarNegacionAnd(padre.right_tree);
		return aux3 || aux4 ;
	}

  }

  private static boolean CambiarNegacionOr(CTree padre)
  {
	CTree hijo,aux1,aux2;
	boolean aux3,aux4;

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_NOT) && (padre.left_tree.type == CNodeType.NODO_OR)){

		hijo=padre.left_tree;
		
		aux1=new CTree();
		aux2=new CTree();
		aux1.Add(hijo.left_tree,"not",CNodeType.NODO_NOT);
		aux2.Add(hijo.right_tree,"not",CNodeType.NODO_NOT);
		padre.Add(aux1,"and",CNodeType.NODO_AND,aux2);
		CambiarNegacionOr(padre);
		return true;

	}else{
		aux3=CambiarNegacionOr(padre.left_tree);
		aux4=CambiarNegacionOr(padre.right_tree);
		return aux3 || aux4;
	}

  }
  private static boolean CambiarNegacionForall(CTree padre)
  {
	CTree hijo;
	boolean aux1,aux2;

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_NOT) && (padre.left_tree.type == CNodeType.NODO_FORALL)){
		hijo=padre.left_tree;
		padre.type=CNodeType.NODO_EXISTS;
		padre.node="exists";
		padre.lista=hijo.lista;
		padre.prolog=hijo.prolog;
		hijo.type=CNodeType.NODO_NOT;
		hijo.node="not";
		CambiarNegacionForall(padre);
		return true;
	}else{
		aux1=CambiarNegacionForall(padre.left_tree);
		aux2=CambiarNegacionForall(padre.right_tree);
		return aux1 || aux2;	}

  }
  private static boolean CambiarNegacionExists(CTree padre)
  {
	CTree hijo;
	boolean aux1,aux2;

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_NOT) && (padre.left_tree.type == CNodeType.NODO_EXISTS)){
		hijo=padre.left_tree;
		padre.type=CNodeType.NODO_FORALL;
		padre.node="forall";
		padre.lista=hijo.lista;
		padre.prolog=hijo.prolog;
		hijo.type=CNodeType.NODO_NOT;
		hijo.node="not";
		CambiarNegacionExists(padre);
		return true;
	}else{
		aux1= CambiarNegacionExists(padre.left_tree);
		aux2= CambiarNegacionExists(padre.right_tree);
		return aux1 || aux2;
	}

  }
  private static void DistribuirNegaciones(CTree arbol)
  {
	boolean hay_cambios=true;
	boolean aux;
	
	while (hay_cambios)
	{
		System.out.println("--------ITERACION-DISTRIBUIR-NEGACIONES---------");

		hay_cambios=CambiarNegacionNegacion(arbol);
		System.out.println("--------negacion-negacion----------");
		
		Utility.pinta_formula(arbol);
		aux= CambiarNegacionAnd(arbol);
		hay_cambios=aux || hay_cambios;
		System.out.println("--------negacion-and----------");
		
		Utility.pinta_formula(arbol);

		if (aux){
			CambiarNegacionNegacion(arbol);
			System.out.println("--------negacion-negacion----------");
			
			Utility.pinta_formula(arbol);

		}

		aux=CambiarNegacionOr(arbol);
		hay_cambios=aux || hay_cambios;
		System.out.println("--------negacion-or----------");
		
		Utility.pinta_formula(arbol);

		if (aux)
		{
			CambiarNegacionNegacion(arbol);
			System.out.println("--------negacion-negacion----------");
			
			Utility.pinta_formula(arbol);

		}

		aux=CambiarNegacionForall(arbol);
		hay_cambios=aux||hay_cambios;
		System.out.println("--------negacion-forall----------");
		
		Utility.pinta_formula(arbol);
		
		if(aux)
		{
                        CambiarNegacionNegacion(arbol);
			System.out.println("--------negacion-negacion----------");
			
			Utility.pinta_formula(arbol);

		}


		hay_cambios=CambiarNegacionExists(arbol) || hay_cambios;
		System.out.println("--------negacion-exists----------");
		
		Utility.pinta_formula(arbol);

	}
  }
  private static  String SustituirVariables (Hashtable tabla, String termino)
  {
	int contador, pos_inicial, pos_final, longitud;
	String inicio,sustituto,sustituido,fin;
	

	pos_inicial=termino.indexOf("$");
	pos_final=termino.indexOf("~",pos_inicial);
	while ((pos_inicial!=-1)&&(pos_final!=-1))
	{
		
		sustituido=termino.substring(pos_inicial+1,pos_final);
		sustituto=(String)tabla.get(sustituido);
		if (sustituto!=null)
		{
			termino= termino.substring(0,pos_inicial+1) + sustituto + termino.substring(pos_final);
			longitud=sustituido.length() - sustituto.length();

		}else{
			longitud=0;
		}
		
		pos_inicial=termino.indexOf("$",pos_final+longitud);
		pos_final=termino.indexOf("~",pos_inicial); 
	}
	return termino;
	
  }

  private static void IntroducirVarTabla(Vector lista, Hashtable tabla, Stack pila)
  {
	int contador,longitud;
	String aux,elemento;
	contador=0;
	longitud=lista.size();
	while (contador<longitud)
	{
	
		elemento=(String)lista.elementAt(contador);
		aux=(String)tabla.put(elemento, "x" + num );

		pila.push(elemento);
		pila.push(aux);

		lista.removeElementAt(contador);
		lista.insertElementAt("x" + num , contador);   

		num++;
		contador++;
	}
  }

  private static void EliminarVarTabla(Vector lista, Hashtable tabla, Stack pila)
  {
	int contador,longitud;
	String aux,elemento;
	contador=0;
	longitud=lista.size();
	while (contador<longitud)
	{
		aux=(String)pila.pop();
		elemento=(String)pila.pop();
		
		tabla.remove(elemento);

		if (aux!=null)
			tabla.put(elemento,aux);

		contador++;
	}
  }

  public static void CambiarNombreVariableCuantificadas(CTree arbol,Hashtable tabla, Stack pila)
  {

	if (arbol.type==CNodeType.NODO_HOJA)
	{
		arbol.node=SustituirVariables(tabla,arbol.node);

	}else	if (arbol.type==CNodeType.NODO_FORALL || arbol.type==CNodeType.NODO_EXISTS)
	{
		IntroducirVarTabla(arbol.lista,tabla,pila);
		CambiarNombreVariableCuantificadas(arbol.left_tree,tabla,pila);
		EliminarVarTabla(arbol.lista,tabla,pila);

		
	}else if (arbol.type==CNodeType.NODO_NOT)
	{
		CambiarNombreVariableCuantificadas(arbol.left_tree,tabla,pila);
	}else{
		CambiarNombreVariableCuantificadas(arbol.left_tree,tabla,pila);
		CambiarNombreVariableCuantificadas(arbol.right_tree,tabla,pila);
	}
  }
  private static boolean SacarCuantificadores( CTree padre)
  {
	CTree hijo,aux_izq,aux_der;
	boolean aux1,aux2;

	

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_OR) && (padre.left_tree.type == CNodeType.NODO_EXISTS ||padre.left_tree.type == CNodeType.NODO_FORALL )){
		hijo=padre.left_tree;
		padre.type=hijo.type;
		padre.prolog=hijo.prolog;
		padre.node=hijo.node;
		padre.lista=hijo.lista;
		hijo.type=CNodeType.NODO_OR;
		hijo.node="or";
		hijo.right_tree=padre.right_tree;
		padre.right_tree=new CTree();
		padre.right_tree.node="null";
		padre.right_tree.type=CNodeType.NODO_HOJA;
		return true;
     }else if ((padre.type == CNodeType.NODO_AND) && (padre.left_tree.type == CNodeType.NODO_EXISTS ||padre.left_tree.type == CNodeType.NODO_FORALL )){
		hijo=padre.left_tree;
		padre.type=hijo.type;
		padre.prolog=hijo.prolog;	
		padre.node=hijo.node;
		padre.lista=hijo.lista;
		hijo.type=CNodeType.NODO_AND;
		hijo.node="and";
		hijo.right_tree=padre.right_tree;
		padre.right_tree=new CTree();
		padre.right_tree.node="null";
		padre.right_tree.type=CNodeType.NODO_HOJA;
		return true;

	}else if ((padre.type == CNodeType.NODO_OR) && (padre.right_tree.type == CNodeType.NODO_EXISTS ||padre.right_tree.type == CNodeType.NODO_FORALL )){
		hijo=padre.right_tree;
		
		aux_izq=new CTree();
		aux_der=new CTree();

		aux_izq.node=padre.left_tree.node;
		aux_izq.type=padre.left_tree.type;
		aux_izq.prolog=padre.left_tree.prolog;
		aux_izq.lista=padre.left_tree.lista;
		aux_izq.left_tree=padre.left_tree.left_tree;
		aux_izq.right_tree=padre.left_tree.right_tree;
		
		aux_der.node=hijo.left_tree.node;
		aux_der.type=hijo.left_tree.type;
		aux_der.prolog=hijo.left_tree.prolog;
		aux_der.lista=hijo.left_tree.lista;
		aux_der.left_tree=hijo.left_tree.left_tree;
		aux_der.right_tree=hijo.left_tree.right_tree;


		padre.type=hijo.type;
		padre.prolog=hijo.prolog;
		padre.node=hijo.node;
		padre.lista=hijo.lista;

		padre.right_tree=new CTree();
		padre.right_tree.node="null";
		padre.right_tree.type=CNodeType.NODO_HOJA;

		padre.left_tree.type=CNodeType.NODO_OR;
		padre.left_tree.node="or";

		padre.left_tree.left_tree=aux_izq;
		padre.left_tree.right_tree=aux_der;
		return true;

      }else if ((padre.type == CNodeType.NODO_AND) && (padre.right_tree.type == CNodeType.NODO_EXISTS ||padre.right_tree.type == CNodeType.NODO_FORALL )){
		hijo=padre.right_tree;

		aux_izq=new CTree();
		aux_der=new CTree();

		aux_izq.node=padre.left_tree.node;
		aux_izq.type=padre.left_tree.type;
		aux_izq.prolog=padre.left_tree.prolog;
		aux_izq.lista=padre.left_tree.lista;
		aux_izq.left_tree=padre.left_tree.left_tree;
		aux_izq.right_tree=padre.left_tree.right_tree;
		
		aux_der.node=hijo.left_tree.node;
		aux_der.type=hijo.left_tree.type;
		aux_der.prolog=hijo.left_tree.prolog;
		aux_der.lista=hijo.left_tree.lista;
		aux_der.left_tree=hijo.left_tree.left_tree;
		aux_der.right_tree=hijo.left_tree.right_tree;


		padre.type=hijo.type;
		padre.prolog=hijo.prolog;
		padre.node=hijo.node;
		padre.lista=hijo.lista;

		padre.right_tree=new CTree();
		padre.right_tree.node="null";
		padre.right_tree.type=CNodeType.NODO_HOJA;

		padre.left_tree.type=CNodeType.NODO_AND;
		padre.left_tree.node="and";

		padre.left_tree.left_tree=aux_izq;
		padre.left_tree.right_tree=aux_der;
		return true;

	}else{

		aux1= SacarCuantificadores(padre.left_tree);
		aux2= SacarCuantificadores(padre.right_tree);
		return aux1 || aux2;
	}

  }
  public static void SacarTodosCuantificadores(CTree arbol)
  {
	boolean hay_cambios=true;
		
	while (hay_cambios)
	{
		System.out.println("--------ITERACION-SACAR-CUANTIFICADORES----------");

		hay_cambios=SacarCuantificadores(arbol);
		
		Utility.pinta_formula(arbol);

		
	}

  }
  private static void Concatenar(Vector lista1, Vector lista2)
  {

	int contador,longitud;
	
	contador=0;
	longitud=lista2.size();
	while(contador<longitud)
	{
		lista1.addElement(lista2.elementAt(contador));
		contador++;
	}
	
  }
  private static void SustituirVariablesExists(CTree arbol, Hashtable tablafuncion)
  {

	if (arbol.type==CNodeType.NODO_HOJA)
	{


		arbol.node=SustituirVariables(tablafuncion,arbol.node);

	}else	if (arbol.type==CNodeType.NODO_FORALL || arbol.type==CNodeType.NODO_EXISTS)
	{

		SustituirVariablesExists(arbol.left_tree,tablafuncion);


		
	}else if (arbol.type==CNodeType.NODO_NOT)
	{

		SustituirVariablesExists(arbol.left_tree,tablafuncion);

	}else{

		SustituirVariablesExists(arbol.left_tree,tablafuncion);
		SustituirVariablesExists(arbol.right_tree,tablafuncion);
	}
  }
  private static CEstructura SustituirUnExists( CTree padre, Vector lista, CEstructura est)
  {
	CTree hijo;
	String aux;
	String funcion_constante;
	Hashtable tablafuncion;
	int longitud,contador=0;

	if (padre.type != CNodeType.NODO_EXISTS && padre.type != CNodeType.NODO_FORALL)
	{	est.hay_cambios=false;
		return est;

	}else if (padre.type == CNodeType.NODO_FORALL){
		
		Concatenar(lista,padre.lista);
		return SustituirUnExists(padre.left_tree,lista,est);

	}else{
		tablafuncion=new Hashtable();

		longitud=padre.lista.size();
		while(contador<longitud)
		{
			if (lista.isEmpty())
			{
				funcion_constante="cte"+est.fun_cte;
			
			}else{
				aux=lista.toString();
				aux="(" + aux.substring(1,aux.length()-1) + ")";
				funcion_constante="f"+ est.fun_cte + aux;
			}

			
			tablafuncion.put(padre.lista.elementAt(contador),funcion_constante);
			contador++;
			est.fun_cte++;
		}

		
		hijo=padre.left_tree;
		padre.Add(hijo.left_tree,hijo.node,hijo.type,hijo.right_tree);
		padre.lista=hijo.lista;
		padre.prolog=hijo.prolog;


		

		SustituirVariablesExists(padre,tablafuncion);
		est.hay_cambios=true;
		return est;
	}

  }
  private static void HacerSkolem(CTree arbol)
  {
	CEstructura est=new CEstructura(0,true);
	Vector lista=new Vector();

	
	while (est.hay_cambios)
	{
		lista.removeAllElements();

		System.out.println("--------ITERACION-SKOLEM----------");

		est=SustituirUnExists(arbol,lista,est);
		
		Utility.pinta_formula(arbol);

	
		
	}

  }
  private static boolean SustituirUnForall(CTree padre)
  {
	CTree hijo;
	String aux;
	int longitud,contador=0;

	if (padre.type != CNodeType.NODO_FORALL)
	{
		return false;

	}else{
		
		hijo=padre.left_tree;
		padre.Add(hijo.left_tree,hijo.node,hijo.type,hijo.right_tree);
		padre.prolog=hijo.prolog;
		padre.lista=hijo.lista;

		return true;
	}

	
  }
  private static void QuitarParaTodos(CTree arbol)
  {
	boolean hay_cambios=true;
	
	while (hay_cambios)
	{
		System.out.println("--------ITERACION-QUITAR-PARATODOS---------");

		hay_cambios=SustituirUnForall(arbol);
		
		Utility.pinta_formula(arbol);

	}

  }

  private static boolean SubirAnd_BajarOr( CTree padre)
  {
	CTree hijo,aux_izq,aux_izq2,aux_der;
	boolean aux1,aux2;

	

	if (padre==null)
		return false;
	else if ((padre.type == CNodeType.NODO_OR) && (padre.left_tree.type == CNodeType.NODO_AND)){


		hijo=padre.left_tree;
		
		aux_izq=new CTree();
		aux_izq2=new CTree();
		aux_der=new CTree();

		aux_izq.node=padre.right_tree.node;
		aux_izq.type=padre.right_tree.type;
		aux_izq.prolog=padre.right_tree.prolog;
		aux_izq.lista=padre.right_tree.lista;
		aux_izq.left_tree=padre.right_tree.left_tree;
		aux_izq.right_tree=padre.right_tree.right_tree;

		aux_izq2.node=padre.right_tree.node;
		aux_izq2.type=padre.right_tree.type;
		aux_izq2.prolog=padre.right_tree.prolog;
		aux_izq2.lista=padre.right_tree.lista;
		aux_izq2.left_tree=padre.right_tree.left_tree;
		aux_izq2.right_tree=padre.right_tree.right_tree;
		
		aux_der.node=hijo.right_tree.node;
		aux_der.type=hijo.right_tree.type;
		aux_der.prolog=hijo.right_tree.prolog;
		aux_der.lista=hijo.right_tree.lista;
		aux_der.left_tree=hijo.right_tree.left_tree;
		aux_der.right_tree=hijo.right_tree.right_tree;


		padre.type=CNodeType.NODO_AND;
		padre.node="and";
		padre.lista=hijo.lista;

	

		padre.right_tree.type=CNodeType.NODO_OR;
		padre.right_tree.node="or";
		padre.left_tree.type=CNodeType.NODO_OR;
		padre.left_tree.node="or";


		padre.right_tree.left_tree=aux_der;
		padre.right_tree.right_tree=aux_izq;
		hijo.right_tree=aux_izq2;
		return true;


     }else if ((padre.type == CNodeType.NODO_OR) && (padre.right_tree.type == CNodeType.NODO_AND )){

		hijo=padre.right_tree;
		
		aux_izq=new CTree();
		aux_izq2=new CTree();
		aux_der=new CTree();

		aux_izq.node=padre.left_tree.node;
		aux_izq.type=padre.left_tree.type;
		aux_izq.prolog=padre.left_tree.prolog;
		aux_izq.lista=padre.left_tree.lista;
		aux_izq.left_tree=padre.left_tree.left_tree;
		aux_izq.right_tree=padre.left_tree.right_tree;

		aux_izq2.node=padre.left_tree.node;
		aux_izq2.type=padre.left_tree.type;
		aux_izq2.prolog=padre.left_tree.prolog;
		aux_izq2.lista=padre.left_tree.lista;
		aux_izq2.left_tree=padre.left_tree.left_tree;
		aux_izq2.right_tree=padre.left_tree.right_tree;
		
		aux_der.node=hijo.left_tree.node;
		aux_der.type=hijo.left_tree.type;
		aux_der.prolog=hijo.left_tree.prolog;
		aux_der.lista=hijo.left_tree.lista;
		aux_der.left_tree=hijo.left_tree.left_tree;
		aux_der.right_tree=hijo.left_tree.right_tree;


		

		padre.type=CNodeType.NODO_AND;
		padre.node="and";
		padre.lista=hijo.lista;

	

		padre.right_tree.type=CNodeType.NODO_OR;
		padre.right_tree.node="or";
		padre.left_tree.type=CNodeType.NODO_OR;
		padre.left_tree.node="or";


		padre.left_tree.left_tree=aux_izq;
		padre.left_tree.right_tree=aux_der;
		hijo.left_tree=aux_izq2;
		return true;

      }else{

		aux1= SubirAnd_BajarOr(padre.left_tree);
		aux2= SubirAnd_BajarOr(padre.right_tree);
		return aux1 || aux2;
	}

  }
  public static void HacerFormaNormalConjuntiva(CTree arbol)
  {
	boolean hay_cambios=true;
		
	while (hay_cambios)
	{
		System.out.println("---ITERACION-FORMA-NORMAL-CONJUNTIVA---");

		hay_cambios=SubirAnd_BajarOr(arbol);
		
		Utility.pinta_formula(arbol);

		
	}

  }
  private static Vector PartirEnClausulas(CTree arbol)
  {
	Vector aux1=new Vector();
	Vector aux2;

	if (arbol.type==CNodeType.NODO_HOJA || arbol.type==CNodeType.NODO_OR || arbol.type==CNodeType.NODO_NOT){
		aux1.addElement(arbol);
		return aux1;
	}else{/***es NODO_AND***/
		aux2=new Vector();
		aux1=PartirEnClausulas(arbol.left_tree);
		aux2=PartirEnClausulas(arbol.right_tree);
		Concatenar(aux1,aux2);
		return aux1;
	}
 }
  public static String	transforma_string(CTree arbol)
  {
		String aux;
		aux=pinta_formula_aux(arbol);
		aux=aux.replace('$',' ');
		aux=aux.replace('~',' ');
		return aux;
  }
  public static Vector ObternerClausulaHorn(CTree arbol)
  {
	Vector lista_clausulas=new Vector();
	tabla_ambito=new Hashtable();
	pila_ambito=new Stack();
	int contador,longitud;
	CTree elemento;
	
	num=0;
		
	QuitarImplicaciones(arbol);
	System.out.println("----EL RESULTADO DE QUITAR LAS IMPLICACIONES ES----");
	
	Utility.pinta_formula(arbol);
	


	QuitarDoblesImplicaciones(arbol);
	System.out.println("----EL RESULTADO DE QUITAR LAS DOBLES IMPLICACIONES ES----");
	
	Utility.pinta_formula(arbol);
	

	DistribuirNegaciones(arbol);
	System.out.println("----EL RESULTADO DE DISTRIBUIR LAS NEGACIONES ES----");
	
	Utility.pinta_formula(arbol);
	

	CambiarNombreVariableCuantificadas(arbol,tabla_ambito,pila_ambito);
	System.out.println("----EL RESULTADO DE CAMBIAR EL NOMBRE VARIABLES CUANTIFICADAS ES----");
	
	Utility.pinta_formula(arbol);
	


	SacarTodosCuantificadores(arbol);
	System.out.println("----EL RESULTADO DE SACAR LOS CUANTIFICADORES ES----");
	
	Utility.pinta_formula(arbol);
	

	HacerSkolem(arbol);
	System.out.println("----EL RESULTADO DE HACER SKOLEM ES----");
	
	Utility.pinta_formula(arbol);
	

	QuitarParaTodos(arbol);
	System.out.println("----EL RESULTADO DE QUITAR LOS PARA TODOS ES----");
	
	Utility.pinta_formula(arbol);
	

	HacerFormaNormalConjuntiva(arbol);
	System.out.println("----EL RESULTADO DE HACER LA FNC ES----");
	
	Utility.pinta_formula(arbol);
	

	lista_clausulas=PartirEnClausulas(arbol);
	System.out.println("----EL RESULTADO DE PARTIR EN CLAUSULAS ES----");

	
	contador=0;
	longitud=lista_clausulas.size();
	String [] clauses=new String[longitud];
	while (contador<longitud)
	{

		System.out.println("----CLAUSULA NUMERO " + contador + " ----");
		elemento=(CTree)lista_clausulas.elementAt(contador);
		clauses[contador]=transforma_string(elemento);
		
		Utility.pinta_formula(elemento);
		contador++;
	}
	return lista_clausulas;



  }
  public static String [] TraducirProlog(Vector arboles)
  {
  	
	int contador=0;
	int longitud=arboles.size();
	String [] prologClauses=new String[longitud];
	CTree arbol_actual=new CTree();	

	while (contador<longitud)
	{
		arbol_actual=(CTree)arboles.elementAt(contador);
			
		if (Es_CH(arbol_actual)==1)
		{
			prologClauses[contador]=TraduceArbol(arbol_actual)+".";
			if (prologClauses[contador].endsWith(":-.")) 
				prologClauses[contador]=prologClauses[contador].substring(0,prologClauses[contador].lastIndexOf(":-."));
		}else{

			prologClauses[contador]=null;
		}
		contador++;
		
	}
	return prologClauses;


  }

  private static int Es_CH(CTree arbol)
  {

	if(arbol.type == CNodeType.NODO_OR){
	
		return Es_CH(arbol.left_tree)+ Es_CH(arbol.right_tree);

	}else if (arbol.type == CNodeType.NODO_NOT){
		
		return 0;

	}else if (arbol.type == CNodeType.NODO_HOJA){

		return 1;

	} 
        return -1;
  }


  private static String TraduceArbol(CTree arbol)
  {
	
	if(arbol.type == CNodeType.NODO_OR){
		
		String izquierda=TraduceArbol(arbol.left_tree);
		String derecha=TraduceArbol(arbol.right_tree);

		if (izquierda.endsWith(":-")){

			return izquierda+derecha;

		}else if(derecha.endsWith(":-")){

			return derecha+izquierda;
		}else{
			return izquierda+","+derecha;
		}
					
	}else if (arbol.type == CNodeType.NODO_NOT){
	
		return arbol.left_tree.prolog;

	}else if (arbol.type == CNodeType.NODO_HOJA){
		
	
		return arbol.prolog + ":-";

	}
 	return null;
	

  }



}
