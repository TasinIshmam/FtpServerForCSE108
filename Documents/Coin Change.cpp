#include<iostream>
#include<vector>
#include<stdio.h>
#define INFINITY 999999



using namespace std;

class Coins{
    int total;
    vector<int> denom;

};

int mincoins(vector<int> coin ,vector<int> denom , int m , int V){

    int notenumber[m+1];
    int table[V+1];
    table[0] =0;
    for(int i = 1; i <=V ;i++){
        table[i] = INFINITY;
    }
    for(int i = 1 ; i<=V ; i++){

        for(int j = 0 ; j < m;j++)
        {
                if( coin[j] <= i){
                    int num = table[i-coin[j]];
                    if(num != INFINITY && num + 1 <table[i] && denom[j] >= num+1){
                        table[i] = num + 1;
                        notenumber[j] = num + 1;
//                        cout<<i<<" : "<<table[i]<<" |";
                    }
                }
        }


//        cout<<endl;
    }
    cout<<endl<<endl;
    for(int i = 0 ; i<=m ; i++){
            if(notenumber[i] == table[V]){
                cout<<coin[i]<<" * "<<notenumber[i];
                break;
            }
//        cout<<notenumber[i]<<" ";
    }


return table[V];


}

int main(){


    int V, n , c,d;
    vector<int> coin, denomination;

    scanf("%d%d",&V, &n);
    for(int i=0 ; i< n ; i++){
        scanf("%d",&c  );
        coin.push_back(c);
    }
    for(int i=0 ; i< n ; i++){
        scanf("%d", &d );
        denomination.push_back(d);
    }

    cout<<endl<<endl<<mincoins(coin, denomination , n , V);




}
