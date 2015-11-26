package com_Cx;
//冒泡、选择、插入、快速排序的实现


public class Sort {

	public static void main(String[] args) {
    int len=10;
    int[] arr1=new int[len];
    for(int i=0;i<len;i++)
    {
     	arr1[i]=(int)(Math.random()*1000);
     }
    Kuaisu paixu=new Kuaisu();
     paixu.sort(0,arr1.length -1,arr1);
	  }
}
class Maopao{
		public void sort(int arr[])
		{
			
			int temp,pass,i;
			for( pass=0;pass<arr.length;pass++)
			{
				for( i=0;i<arr.length -pass-1;i++)
				{
					if (arr[i]>arr[i+1])
					   {
						temp=arr[i];
						arr[i]=arr[i+1];
						arr[i+1]=temp;	
					    }
				}
				for(i=0;i<arr.length;i++)
				{
					System.out.print(arr[i]+" ");
				}
				System.out.println();
			}
			
		}
	}
class Xuanze
	{
		int temp,i,j,k;
		public void sort(int[] arr)
		{
		   for( j=0;j<arr.length-1;j++)
		   {
			   for(i=j+1;i<arr.length;i++ )
			   {k=j;
				  if(arr[i]<arr[k])k=i;
				  temp=arr[k];
				  arr[k]=arr[j];
				  arr[j]=temp;
			   }
			   for(i=0;i<arr.length;i++)
				{
					System.out.print(arr[i]+" ");
				}
				System.out.println();
		   }	
		}
	}
class Insert{
	public void sort(int[] arr)
	{
		int pass,i,temp;
		for(pass=1;pass<arr.length ;pass++)
		{
			temp=arr[pass];
			for(i=pass-1;i>=0;i--)
				if(arr[i]<=temp)
					break;
				else
					arr[i+1]=arr[i];
				arr[i+1]=temp;
			for(i=0;i<arr.length;i++)
			{
				System.out.print(arr[i]+" ");
			}
			System.out.println();
		}
	}
}
class Kuaisu{
	public void sort(int left,int right,int[] arr)
	{
		int l=left;
		int r=right;
		int pivot=arr[(left+right)/2];
		int temp=0;
		while(l<r)
		{
			while(arr[l]<pivot)l++;
			while(arr[r]>pivot)r--;
			
			if(l>=r)break;
			
			temp=arr[l];
			arr[l]=arr[r];
			arr[r]=temp;
			
			if(arr[l]==pivot)--r;
			if(arr[r]==pivot)++l;
		}
		if(l==r){
			l++;
			r--;
		}
		if(left<r) sort(left,r,arr);
		if(right>l) sort(l,right,arr);
		for (int i=0;i<arr.length ;i++){
			System.out.print(arr[i]+"  ");	
		}
		System.out.println();
	}
}
