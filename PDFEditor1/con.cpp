//author-pravisadi
//for every question there is a file named qf[0 to n-1] 
//tick cross circle quesm marks fullmarks
#include<algorithm>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
float f[200];
int a[200][4];
float tx[4]={0.0};
int n,ns,nf;
float matn[4][4],matd[4][4];
float coeff[50][4];
void cal(int);
FILE *fw;
float det(float m[][4]);
void rshuffle(void);
int maine(char *fname);
void chkcon(void);
int main(int argc,char** arg)
{
		nf=atoi(arg[1]);
		char fn[10]="qf0";
		int i;
		fw=fopen("rescon","w+");
		for(i=0;i<nf;i++) {
			fn[2]='0'+i;
			maine(fn);
		}
		fclose(fw);
		chkcon();
}
int maine(char *fname)
{
	int fm,m,i=0,j,k,l;
	int q,r,c;
	FILE *fp=fopen(fname,"r");
	tx[0]=tx[1]=tx[2]=tx[3]=0.0;
	n=ns=0;
	if(fp==NULL) perror("fp:");
	while(!feof(fp)) {
		//puts("hiii");
		fscanf(fp,"%d %d %d %d %d %d",&m,&fm,&a[i][0],&a[i][1],&a[i][2],&a[i][3]);
		f[i]=(1.0*m)/fm;
		//printf("%d %d %d %d %d %d\n",a[i][0],a[i][1],a[i][2],a[i][3],m,fm);
		i++;
		}
	i--;
	ns=i;
	printf("ns =%d\n",ns);
	for(c=1;c<=4;c++) {
		j=1;
		rshuffle();
		//puts("loop 1");
		for(i=0;i<=(ns-4);i++) {
			//puts("loop 2");
			for(k=0;k<4;k++) {
				///puts("loop 3a");
				matd[k][0]=1.0*a[(i+j*k)%ns][0];
				matd[k][1]=1.0*a[(i+j*k)%ns][1];
				matd[k][2]=1.0*a[(i+j*k)%ns][2];
				matd[k][3]=1.0*a[(i+j*k)%ns][3];
			}
			/*printf("matrix denominator\n");
			for(q=0;q<4;q++) {
				for(r=0;r<4;r++) 
					printf("%f ",matd[q][r]);
				printf("\n");
				}
			printf("detrminant\n %f\n",det(matd));*/
			for(l=0;l<4;l++) {
				for(k=0;k<4;k++) {
				matn[k][l]=f[(i+j*k)%ns];
				if(l!=0) matn[k][0]=1.0*a[(i+j*k)%ns][0];
				if(l!=1) matn[k][1]=1.0*a[(i+j*k)%ns][1];
				if(l!=2) matn[k][2]=1.0*a[(i+j*k)%ns][2];
				if(l!=3) matn[k][3]=1.0*a[(i+j*k)%ns][3];
				}
				/*printf("matrix numerator\n");
				for(q=0;q<4;q++) {
					for(r=0;r<4;r++) 
						printf("%f ",matn[q][r]);
					printf("\n");
				}*/
				
				cal(l);
			}
			
		}
	}
	printf("n=%d\n",n);
	n=n>>2;
	tx[0]/=n;tx[1]/=n;tx[2]/=n;tx[3]/=n;
	fprintf(fw,"%f %f %f %f\n",tx[0],tx[1],tx[2],tx[3]);
	fclose(fp);
	return 0;
}

void cal(int i)
{
	float v=det(matn),r=det(matd);
	if(r!=0) {
		v=v/r;
		tx[i]+=v;
		n++;
		}
	return;
}

float det(float m[][4])
{
	int f=1,i,j,k,r,c,q,p;
	float det=0.0,v,cf[3][3];
	/*printf("mat denominator\n");
	for(q=0;q<4;q++) {
		for(p=0;p<4;p++) 
			printf("%f ",m[q][p]);
		printf("\n");
	}*/
	for(i=0;i<4;i++) {
		r=0;
		for(j=1;j<4;j++) {
			c=0;
			for(k=0;k<4;k++) {
				//printf("%d%d ",r,c);
				if(k!=i) cf[r][c++]=m[j][k];
				}
			//printf("\n");
			r++;
			}
		/*puts("cofactor matrix");
		for(q=0;q<3;q++) {
			for(p=0;p<3;p++) 
				printf("%f ",cf[q][p]);
			printf("\n");
		}*/
		v=cf[0][0]*(cf[1][1]*cf[2][2]-cf[1][2]*cf[2][1])-cf[0][1]*(cf[1][0]*cf[2][2]-cf[1][2]*cf[2][0])+cf[0][2]*(cf[1][0]*cf[2][1]-cf[2][0]*cf[1][1]);
		//printf("cf = %f\n",v);
		det+=f*v*m[0][i];
		f=-f;
	}
	return det;
}

void rshuffle(void)
{
	float tf;
	int i,r,j,t;
	for(i=ns-1;i>0;i--) {
		r=rand()%i;
		for(j=0;j<4;j++) {
			t=a[r][j];
			a[r][j]=a[i][j];
			a[i][j]=t;
		}
		tf=f[r];f[r]=f[i];f[i]=tf;	
	}
	return;
}

void chkcon(void)
{
	char regn[20];
	FILE *fp[50];
	FILE *res=fopen("qresult","w+");
	FILE *list=fopen("qreg","r");
	int i,t,x,c,q,fm,m,fullm,ttl;
	float v,ex;
	bool flagc=0;
	char fn[10]="qf0";
	fw=fopen("rescon","r");
	for(i=0;i<nf;i++) {
		fscanf(fw,"%f %f %f %f",&coeff[i][0],&coeff[i][1],&coeff[i][2],&coeff[i][3]);
		fn[2]='0'+i;
		fp[i]=fopen(fn,"r");
	}
	while(fscanf(list,"%s",regn)!=-1) {
		
		ttl=fullm=0;
		ex=0.0;
		for(i=0;i<nf;i++) {
			fscanf(fp[i],"%d %d %d %d %d %d",&m,&fm,&t,&x,&c,&q);
			//printf("co::%f %f %f %f\n",coeff[i][0],coeff[i][1],coeff[i][2],coeff[i][3]);
			//printf("st::%d %d %d %d %d %d\n",m,fm,t,x,c,q);
			v=coeff[i][0]*t+x*coeff[i][1]+c*coeff[i][2]+q*coeff[i][3];
			ttl+=m;
			fullm+=fm;
			ex+=v*fm;
		}
		if((fabs(ex-1.0*ttl))>=0.1*fullm) {
			if(flagc==0) { fprintf(res,"Inconsistency Found\n"); flagc=1;}
			fprintf(res,"%s %.0f %d %d\n",regn,ex,ttl,fullm);
			}
	}
	if(flagc==0) fprintf(res,"Consistent checking\n");
	return;
}
