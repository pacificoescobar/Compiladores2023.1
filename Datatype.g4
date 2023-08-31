grammar Datatype;

program : datatype* EOF ; 

datatype : 'datatype' ID '(' field+  ')' ';' ;

field : ID ID (',')? ;

ID : [a-zA-Z]+ ;
ESPACOS : (' '|'\n') -> skip ; 
