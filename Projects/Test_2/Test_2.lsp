(defun find_elem (x y)
 (loop for elem in y
  if (equal elem x) return x
  finally (return nil)
))

(defun arr_count (x count)
  (loop for elem in x
        do (setq count (+ count 1))
        finally (return count)))
(find_elem  1 `(1 2 3))

(defun func ()
  (find_elem  3`(1 2 3))
)
(func)

