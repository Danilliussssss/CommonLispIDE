(defun arr_count (x count) 
 (cond 
 ((null x) count) 
 (T (arr_count (cdr x) (+ 1 count))) 
)) 
 
(defun arr_2_count (x max) 
(cond 
((null x) max) 
(( >(arr_count (car x) 0) (arr_count max 0)) (arr_2_count (cdr x) (car x))) 
(( <= (arr_count (car x) 0) (arr_count max 0)) (arr_2_count (cdr x) max)) 
)) 
 
(arr_2_count '((1 2 3 4 5) (6 9 2 1)) '()) 
(arr_2_count '((1 2 3) (6 9 2 1 4)) '()) 
(arr_2_count '((a b c) ()) '())