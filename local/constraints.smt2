(set-option :timeout 5000)
(declare-fun tvw_p (Int) Int)
(assert(> (tvw_p 0 )  0 ) )
(check-sat)(get-model)
