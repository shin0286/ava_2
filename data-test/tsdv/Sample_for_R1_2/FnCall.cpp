
int mmin(int x, int y){
	if  (x < y)
		return x;
	else if (x > y)
		return y;
	else
	    return 1;
}

int mmin3(int x, int y, int z){
    int r = mmin(x,y);
//    int r = (x<y)?x:y;
//    int i;
//    for (i=0; i<10; i++){
//        cout<<i;
//    }
	if (r < z)
		return r;
	else if (r == z)
	    return 1;
	else
		return z;
}

int test1(int x, int y){
    int r = (x+y > 0)?(x+y):(x+y < 0)?(x-y):x;
    if (r-2 > 0)
        return r;
    else
        return r+2;
}
// Calculate 1 + 2 + ... + m
int SumRecursive(int m){
	if (m <= 0)
		return 0;
	else
		return m + SumRecursive(m-1);
}
