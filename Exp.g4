grammar Exp;

NUM : [0-9]+('.'[0-9]+)? ;
SOMA : '+' ;
ESPACOS : (' '|'\n') -> skip ; 

program : exp EOF ; 

exp : NUM 
    | exp SOMA exp
    ;

