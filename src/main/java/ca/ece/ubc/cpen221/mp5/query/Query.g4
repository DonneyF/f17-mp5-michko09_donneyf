grammar Query;

// First we define all tokens (terminals)

OR : '||';
AND : '&&';
GT : '>';
GTE : '>=';
LT : '<';
LTE : '<=';
EQ : '=';
NUM : [1-5] | [1-5] ('.' [0-9])?;
LPAREN : '(';
RPAREN : ')';
STRING : [A-Za-z0-9'.]+ ( ' ' [A-Za-z0-9'.]+ | ' ' '&' ' ' [A-Za-z0-9'.]+ | '-' [A-Za-z0-9'.]+ | ' ' '-' ' ' [A-Za-z0-9'.]+ | '\u00e9' )*;
WS : [ \t\r\n]+ -> skip ;

// Next we define non terminals

orExpr : andExpr ( OR andExpr )*;
andExpr : atom ( AND atom )*;
atom : in | category | rating | price | name | LPAREN orExpr RPAREN;
ineq : GT | GTE | LT | LTE | EQ;
in : 'in' LPAREN STRING RPAREN;
category : 'category' LPAREN STRING RPAREN;
name : 'name' LPAREN STRING RPAREN;
rating : 'rating' ineq NUM;
price : 'price' ineq NUM;