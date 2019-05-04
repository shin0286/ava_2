(set-option :timeout 5000)
(declare-fun tvw_x () Int)
(declare-fun tvw_y () Int)
(declare-fun tvw_z () Int)
(assert(not (< tvw_x tvw_y ) ) )
(assert(not (< (+ tvw_y 0 ) tvw_z ) ) )
(assert(not (= (+ tvw_y 0 ) tvw_z ) ) )
(check-sat)(get-model)
