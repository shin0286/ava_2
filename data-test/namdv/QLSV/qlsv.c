#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<string.h>
struct SinhVien
{
    char Ten[30];
    char MSSV[5];
    float dGK,dTK,dCK;
    char XepLoai[15];
};
void TaoDSSV(SinhVien a[], int n)
{
    for (int i=0; i < n; i++)
    {
           for (int j=0; j < 11; j++)
               a[i].Ten[j] = 65 + rand()%25; // random ngay nhien ku ty vao ten
        a[i].Ten[11] = 0;
        for (int j=0; j < 5; j++)
               a[i].MSSV[j] = 65 + rand()%25;// random ngay nhien ku ty vao mssv
        a[i].MSSV[5] = 0; // ky tu cuoi cung cua chuoi
        a[i].dTK = rand() % 10;
        a[i].dGK = rand() % 10;
    }
}
void XuatDSSV(SinhVien a[], int n)
{
       printf("\n--------------------DS sinh vien-------------------\n");
    printf("\n%5s | %10s   %8s %5s","MSSV","Ho Va Ten","Diem TK", "Diem GK");
    for (int i=0; i < n; i++)
        printf("\n%-5s | %s    %4.2f    %4.2f",a[i].MSSV,a[i].Ten,a[i].dTK, a[i].dGK);
        // xuat thong tin sinh vien
}
void XuatDSDT(SinhVien a[], int n)
{
    printf("\n----------------DS duoc thi cuoi ky----------------\n");
    printf("\n%5s | %10s   %8s %5s","MSSV","Ho Va Ten","Diem TK", "Diem GK");
    for (int i=0; i < n; i++)
       {
           if( a[i].dGK >= 3)
            printf("\n%-5s | %s    %4.2f    %4.2f",a[i].MSSV,a[i].Ten,a[i].dTK, a[i].dGK);
       }
}
void NhapDCK(SinhVien a[], int n)
{
//    printf("\n----------------NHAP DIEM CUOI KY----------------\n");
//    printf("\n%5s | %10s   %8s %5s %8s","MSSV","Ho Va Ten","Diem TK", "Diem GK","Diem CK");
    for (int i=0; i < n; i++)
    {
       //    if( a[i].dGK >= 3)
//        {
//            printf("\n%-5s | %s    %4.2f    %4.2f     ",a[i].MSSV,a[i].Ten,a[i].dTK, a[i].dGK);
//            scanf("%f",&a[i].dCK);
//        }
/// khuc o tren tu nhap diem cuoi ky = tay, xuat ra day sach roi nhap nhu bang diem cua gv
        if( a[i].dGK >= 3)
            a[i].dCK = rand() % 10;
        // gan ngau nhien gia tri diem cuoi ky luon
        // 2 cai tieu de o tren la nhap = tay
       }
}
void XuatKQ(SinhVien a[], int n)
{
    printf("\n------------------KQ Sau khi ket thuc mon hoc------------------\n");
    printf("\n%5s | %10s   %8s %5s %8s %7s %9s","MSSV","Ho Va Ten","Diem TK", "Diem GK","Diem CK","Ket Qua","Xep Loai");
    for (int i=0; i < n; i++)
    {
        if(a[i].dGK < 3)
        {
            printf("\n%-5s | %s    %4.2f    %4.2f    %4d   %5s",a[i].MSSV,a[i].Ten,a[i].dTK,a[i].dGK, 0,"Rot");
        }
           float dtb = (a[i].dTK*2+a[i].dGK*3+a[i].dCK*5)/10;
           if(a[i].dGK >= 3 && dtb < 4)
               printf("\n%-5s | %s    %4.2f    %4.2f    %4.2f   %5s",a[i].MSSV,a[i].Ten,a[i].dTK,a[i].dGK, a[i].dCK,"Rot");
        if(a[i].dGK >= 3 && dtb >= 4)
        {

            if(dtb < 5)
                   strcpy(a[i].XepLoai,"Yeu");
               if(dtb >= 5 && dtb < 6.5 )
                 strcpy(a[i].XepLoai,"Trung Binh");
             if(dtb >= 6.5 && dtb < 8)
                 strcpy(a[i].XepLoai,"Kha");
            if(dtb >= 8 )
                strcpy(a[i].XepLoai,"Gioi");
            printf("\n%-5s | %s    %4.2f    %4.2f    %4.2f   %5s\t%s",a[i].MSSV,a[i].Ten,a[i].dTK,a[i].dGK, a[i].dCK,"Dau",a[i].XepLoai);
        }
    }
}
int check(int a[], int spt, int GT)
{
    for(int i = 0 ; i < spt ; i ++)
        if(a[i] == GT)
            return 0;
    return 1;
}
void Xuat1SV(SinhVien a)
{
    printf("\n%-5s | %s    %4.2f    %4.2f    %4.2f   %5s\t%s",a.MSSV,a.Ten,a.dTK,a.dGK, a.dCK,"Dau",a.XepLoai);
}
void XuatSVDK(SinhVien a[], int n)
{

    int dem = 0;
    for(int i = 0 ; i < n ; i ++ )
    {
        if(a[i].dGK >= 3)
        {
            if((a[0].dTK*2+a[0].dGK*3+a[0].dCK*5)/10 >= 4 )
                dem++;
        }
    }
    if(dem ==0)
    {
        printf("\n-------------------Khong co sv nao dc khen----------------------\n");
        return ;
    }
    printf("\n-----------------------DS SV dc khen thuong --------------------\n");
    printf("\n%5s | %10s   %8s %5s %8s %7s %9s","MSSV","Ho Va Ten","Diem TK", "Diem GK","Diem CK","Ket Qua","Xep Loai");
    if(dem != 5)
    {
        if(dem > 5)
            dem = 5;
        int b[5],spt=0;
        while(dem != 0)
        {
            float dtb = (a[0].dTK*2+a[0].dGK*3+a[0].dCK*5)/10;
            float max = dtb;
            SinhVien tmp;
            int tmp1;
            for(int i = 0 ; i < n ; i ++ )
            {
                if(a[i].dGK < 3)
                    continue;
                dtb = (a[i].dTK*2+a[i].dGK*3+a[i].dCK*5)/10;
                if(max <= dtb && check(b,spt,i)==1)
                {
                    max = dtb;
                    tmp = a[i];
                    tmp1 = i;
                }
            }
            b[spt] = tmp1;
            spt++;
            dem--;
            Xuat1SV(tmp);
        }
    }
}
void ThongKe(SinhVien a[], int n)
{
    int demG = 0,demK=0,demTB=0,demCT=0,demTR=0;
    for(int i = 0 ; i < n ; i ++ )
    {
        if(a[i].dGK >= 3)
        {
            float dtb = (a[i].dTK*2+a[i].dGK*3+a[i].dCK*5)/10;
            if(dtb < 4 )
                demTR++;
            if(dtb >= 8)
                demG++;
            if(dtb<8 && dtb >=6.5)
                demK++;
            if(dtb>5 && dtb < 6.5)
                demTB++;
        }
        if(a[i].dGK < 3)
            demCT++;
    }
    printf("\nPhan tram so sv bi cam thi la: %.3f \n",(float)(100*demCT/n));
    printf("Phan tram so sv thi rot la: %.3f \n",(float)(100*demTR/n));
    printf("Phan tram so sv xep loai trung binh la: %.3f \n",(float)(100*demTB/n));
    printf("Phan tram so sv xep loai kha la: %.3f \n",(float)(100*demK/n));
    printf("Phan tram so sv xep loai gioi la: %.3f \n",(float)(100*demG/n));
}
void SapXep(SinhVien a[], int n)
{
    int dem =1;
    int n1 = n;
    for(int i = 0 ; i < n1 ; i ++ )
        if(a[i].dGK < 3)
         {
             SinhVien tmp = a[i];
             a[i] = a[n-dem];
             a[n-dem] = tmp;
             dem++;
             i--;
             n1--;
         }
     // dua het nhung dua nao bi cam thi ra sau cung;
    for(int i = 0 ; i < n-dem+1 ; i ++)
    {
        for(int j = i ; j < n-dem+1 ; j++)
        {
            if((a[i].dTK*2+a[i].dGK*3+a[i].dCK*5) < (a[j].dTK*2+a[j].dGK*3+a[j].dCK*5))
            {
                SinhVien tmp = a[j];
                a[j] = a[i];
                a[i] = tmp;
            }
        }
    }
    printf("\n========================DA XEP XONG===========================");
    // li do tai sao phai xep nhung dua bi cam thi ra dang sau//
    // nhung dua bi cam thi thi k co diem cuoi ki
    //  nen k tinh dc diem trung binh
    // o day co the cho dua nao cam thi co diem cuoi ky la 0 nhung nhu the thi don gian qua
}
int menu()
{
    printf("\n+++++++++++++++++++++++CHON DI++++++++++++++++++++++++++++++\n");
    printf("1.Tao danh sach sinh vien \n");
    printf("2.Xem nhung sinh vien duoc thi cuoi ki\n");
    printf("3.Nhap diem thi cuoi ki\n");
    printf("4.Xem ket qua cua mon hoc\n");
    printf("5.Xem ds sinhvien duoc khen htuong\n");
    printf("6.Thong ke danh sach\n");
    printf("7.Sap Xep\n");
    printf("-------------------------0.THOAT-----------------------------\n");
    int n;
    scanf("%d",&n);
    return n;
}
int main()
{
    int n ;
    scanf("%d",&n);
    SinhVien a[n];
    srand(time(NULL));
   int chon;
    do
       {
        chon = menu();
        switch(chon)
        {
            case 1:
                TaoDSSV(a,n);
                printf("\nDa Tao Xong\n");
                break;
            case 2:
                XuatDSDT(a,n);
                break;
            case 3:
                NhapDCK(a,n);
                printf("\nDa Nhap tu dong xong\n");
                break;
            case 4:
                XuatKQ(a,n);
                break;
            case 5:
                XuatSVDK(a,n);
                break;
            case 6:
                ThongKe(a,n);
                break;
            case 7:
                SapXep(a,n);
                break;
            case 0:
                printf("+++++++++++++++++++++++++++Thoat+++++++++++++++++++++++++++");
                break;
            default:
                printf("chon sai roi ");
                break;
        }
   }while(chon != 0);
   printf("\n\n");
}