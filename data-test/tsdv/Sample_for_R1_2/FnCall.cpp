
int s;

int mmin(int x, int y){
	if  (x < y)
		return x;
	else
		return y;
}

int mmin3(int x, int y, int z){
//    int p;
//    int q;
//    q = x;
//    x = q + y;
    int r = mmin(x,y);
//    if (x < r) {
//        return 0;
//    }
	if (r < z)
		return r;
	else if (r == z)
	    return 1;
	else
		return z;
}

int test0(int x, int y){
    int r = (x+y > 0)?(x+y):(x+y < 0)?(x-y):1;
    return r;
}
int test1(int x, int y){
    int r = test0(x,y);
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

int main(){
    int x = 1;
    int y = 3;
    int z = 2;
    int r = mmin3(x,y,z);
    return 0;
}
