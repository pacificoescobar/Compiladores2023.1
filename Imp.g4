grammar Imp;

program : com EOF ; 

exp : NUM  #Const
    | ID #Var
    | exp OP exp #Op
    | '(' exp ')' #Group
    ;

com : ID '=' exp ';' #Atrib
    | 'print' exp ';' #Print
    | '{' com* '}' #Seq
    | 'if' '(' exp ')' com 'else' com #If
    | 'while' '(' exp ')' com #While
    ;

ID : [a-zA-Z]+ ;
NUM : [0-9]+('.'[0-9]+)? ;
OP : '+' | '-' | '*' | '/' | '==' | '!=' ;
ESPACOS : (' '|'\n') -> skip ; 
