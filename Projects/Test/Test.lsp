(defun example (x y)
  (let ((z (+ x y)))
    (break "Переход в режим отладки")
    z))

(example 10 20)
