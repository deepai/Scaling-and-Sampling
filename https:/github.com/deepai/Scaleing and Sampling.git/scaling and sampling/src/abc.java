import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class abc {
	
	String outpath="//home//deepai//Documents";
	Scanner in;
	String listfile;
	File f,g;
	temp temp;
	FileReader tempA;
	BufferedReader br,br2;
	ArrayList<Float> _drawn;  //Store the points here
	float[] pathscale;
	int count;
	double scale;
	double height_dflt;
	double width_dflt;
	FileOutputStream fout;
	OutputStreamWriter fott;
	int ct=0; //for counting the index of writing files
	int a[]={0,0,0,0,0,0,0};
	int[] angleRange={0,0,0,0,0,0};
	String foldername="empty";
	int noofPoints_Equidistant;  //
	String file1=null,file2=null; //two filenames for DTW purpose 
	
	ArrayList<temp> list;
	abc(double a,double b)
	{
		height_dflt=a;
		width_dflt=b;
	}
	public void read() throws Exception
	{
		
		_drawn=new ArrayList<Float>();
		System.out.println("Enter the fileslist");

		in=new Scanner(System.in);
		listfile=in.nextLine();
		
		
		/*Specify number of points manually
		 System.out.println("Enter number of points");
		 noofPoints_Equidistant=Integer.parseInt(in.nextLine());
		 */
		
		
		f=new File(listfile);
		tempA=new FileReader(f);
		br=new BufferedReader(tempA);
		String read;
		ArrayList<String> files=new ArrayList<String>();
		while((read=br.readLine())!=null)
		{
			files.add(read);
		}
		File k;
		String fileclass="-1";
		for(int i=0;i<files.size();i++)
		{
			
			
			 //for obtaining the class of the files
			try{
				
			
			//String[] temp=(files.get(i)).split("\\\\");
			//System.out.println(files.get(i));
			/*for(int q=0;q<temp.length;q++)
			{
				System.out.println(temp[q]);
			}*/
			//String path="G:\\training\\training";  //change input folder here...
			//g=new File(path+"\\"+temp[3]+"\\"+temp[4]);
			g=new File(files.get(i));
			String[] tmpname=files.get(i).split("\\\\");
			String[] filename=tmpname[5].split("\\.");
			String Div_class=filename[0].split("_")[3];
			if(!(Div_class.equals(fileclass)))
			{
					if(!fileclass.equals("-1"))
					{
						fott.close();
						fout.close();
					}
					fileclass=Div_class;
					System.out.println(outpath+"\\"+Div_class+".txt");
					k=new File(outpath+"\\"+Div_class+".txt");
					System.out.println(k.exists());
					k.createNewFile();
					fout=new FileOutputStream(k,true);
					fott=new OutputStreamWriter(fout);
			}
			
			
			/*if(!temp[3].equals(foldername)) //check if folder exists
			{
				new File(outpath+"\\"+temp[3]).mkdir();
				foldername=temp[3];
			}*/
			
			FileReader temp1 = new FileReader(g);
			br2=new BufferedReader(temp1);
			String readPoints;
			 count=0;
			 int cts=0;
			
			while((readPoints=br2.readLine())!=null)
			{
				if(count==0)
				{
					String[] count=readPoints.split("=");
					cts=Integer.parseInt(count[1].replaceAll("\\s+",""));
				}
				if(count<2)
				{
					count++;
					continue;
				}
				count++;
				//System.out.println(readPoints);
				String[] tempBuffer=readPoints.split("\\s+");
			//System.out.println("the points are "+tempBuffer[1]+","+tempBuffer[2]+":"+count);
				_drawn.add(Float.parseFloat(tempBuffer[1]));
				_drawn.add(Float.parseFloat(tempBuffer[2]));
				if((count-2)==cts)
					break;
				
			}
			
			count=count-2;
			
			if(count<16)
				a[0]++;
			else if(count>=16 && count <32)
				a[1]++;
			else if(count>=32 && count <48)
				a[2]++;
			else if(count>=48 && count <64)
				a[3]++;
			else if(count>=64 && count <80)
				a[4]++;
			else if(count>=80 && count <96)
				a[5]++;
			else if(count>=96 && count <112)
				a[6]++;
			
			//System.out.println(path+"\\"+temp[5]+"\\"+temp[6]+":"+count);
			//call scale function

				br2.close();
				temp1.close();
				float[] finalpnts;
				scale();	
				getstrokes getsizeobj=new getstrokes();
				int size=getsizeobj.getsize();//setting size of the array and storing returned value from the strokes function
				double[] criticalpts=new double[size];
				
				
				
				//finalpnts=getsizeobj.strokes(pathscale);
				
				//Here we are saving the files
				
				
					/*
					g=new File(outpath+"\\"+temp[6]);
					g.createNewFile();
					fout=new FileOutputStream(g,true);
					fott=new OutputStreamWriter(fout);
					for(int p=0;p<finalpnts.length/2;i++)
					{
						fott.append(finalpnts[2*p]+","+finalpnts[2*p+1]+"\n");
					}

				
					
					if(file1!=null)
					{
						file1=outpath+"\\"+temp[6];
					}
					else
						file2=outpath+"\\"+temp[6];
					fott.close();
					fout.close();
					*/
					
					finalpnts=smoothFunction(); //to manually set number of points override the method with smoothFunction(noofPoints_Equidistant);
					
			
				/*THE ONES USED PREVIOUSLY IMPORTANT
				
				count=finalpnts.length/2;
				//g=new File(outpath+"\\"+temp[3]+"\\"+temp[4]);  //outpath for redundant,path for original
				g=new File(outpath+"\\"+g.getName());
				g.createNewFile();
				fout=new FileOutputStream(g,true);
				fott=new OutputStreamWriter(fout);
				
				*/
				for(int p=0;p<finalpnts.length/2;p++)
				{
					fott.append(" "+String.format("%.02f",finalpnts[2*p])+" "+String.format("%.02f",finalpnts[2*p+1]));
				}
				fott.append("\n");
				
				/*
				IMPORTANT
				
				*/
				
				
				/* currently USed
				fott.append("No Of Points = "+count+"\n");
				fott.append(" Absci Ordin Press PenWid\n");
				for(int j=0;j<(finalpnts.length-1);j+=2)
				{
					String x=String.format("%5d %5d %5d %5d\n",(int)finalpnts[j],(int)finalpnts[j+1],1,50);
					fott.append(x);
				}
				 currently USed */
				//fott.close();
				//fout.close();
					
				}catch(Exception e)
				{
					System.out.println(e);
				}
				
				/*for(int a=0;a<size;a++)
				{	
						System.out.println(criticalpts[a]);
				}*/
				/*
				if((pathscale.length/2)<=35)
					finalpnts=pathscale;
				else
					finalpnts=temporalsampling(pathscale,30);   //replace by pathscale or removeRedundant()
					*/
				/*
				finalpnts=getsizeobj.strokes(pathscale);
				count=finalpnts.length/2;
				g=new File(outpath+"\\"+temp[6]);  //outpath for redundant,path for original
				g.createNewFile();
				fout=new FileOutputStream(g,true);
				fott=new OutputStreamWriter(fout);
				fott.append("No Of Points = "+count+"\n");
				fott.append(" Absci Ordin Press PenWid\n");
				for(int j=0;j<finalpnts.length;j+=2)
				{
					String x=String.format("%5d %5d %5d %5d\n",(int)finalpnts[j],(int)finalpnts[j+1],1,50);
					fott.append(x);
				}
				fott.close();
				fout.close();
			*/
			if(i==(files.size()-1))
			{
				//fott.close();
				//fout.close();
			}
			
		}
		
		
		for(int i=0;i<angleRange.length;i++)
		{
			//System.out.println("case "+i+":"+angleRange[i]);
		}
		/*for(int i=0;i<7;i++)
		{
			System.out.println(i*16+"-"+(i+1)*16+":"+a[i]);
		}*/
		
	}
	public double calculateDist(float x1, float x2, float y1,float y2){
		return Math.sqrt(((y2-y1)*(y2-y1))+((x2-x1)*(x2-x1)));//calculate the euclidean distance between each pair of consecutive points
	}
	public float[] removeRedundant()
	{
		int calc=0;
		double totalDistance=0;
		double distance=0;
		double meandistance;
		
		double lastdistance;
		
		int numPoints=pathscale.length;
		if((numPoints/2)<=16)
			return pathscale;
		ArrayList<Float> finalVector=new ArrayList<Float>();
		for(int pt=3;pt<=(numPoints-1);pt++)
		{
			float y2=pathscale[pt];
			float y1=pathscale[pt-2];
			float x2=pathscale[pt-1];
			float x1=pathscale[pt-3];
			totalDistance+=calculateDist(x1, x2, y1, y2); 
		                          //calculate mean distance
		}
		meandistance=totalDistance/((16));
		//System.out.println("meandistance="+meandistance);
		finalVector.add(pathscale[0]);
		finalVector.add(pathscale[1]);
		int b4index=0,tempIndex=0;                        //the 1st point 
		for(int index=5;index<(numPoints-3);index++)   //index is always the second point
		{
			
			float y2=pathscale[index];
			float y1=pathscale[index-2];
			float x2=pathscale[index-1];
			float x1=pathscale[index-3];
			lastdistance=distance;
			distance=distance+Math.sqrt(((y2-y1)*(y2-y1))+((x2-x1)*(x2-x1)));   //distance between consecutive points
			
			double mnext=distance%meandistance;
			double mprev=lastdistance%meandistance;
			
			if(mnext<mprev)
			{
				finalVector.add(x1);
				finalVector.add(y1);
				calc++;
				
			}
			
			
		}
		finalVector.add(pathscale[pathscale.length-2]);
		finalVector.add(pathscale[pathscale.length-1]);
		//System.out.println(numPoints+":"+finalVector.size()/2);
		float[] finalvals=new float[finalVector.size()];
		for(int i=0;i<finalVector.size();i++)
		{
			finalvals[i]=finalVector.get(i);
		}
		
		return finalvals;
	}
	
	/*
	public float[] removeRedundant()
	{
		double totalDistance=0;
		double distance=0;
		double meandistance;
		int numPoints=pathscale.length;
		if((numPoints/2)<=16)
			return pathscale;
		ArrayList<Float> finalVector=new ArrayList<Float>();
		for(int pt=3;pt<=(numPoints-1);pt++)
		{
			float y2=pathscale[pt];
			float y1=pathscale[pt-2];
			float x2=pathscale[pt-1];
			float x1=pathscale[pt-3];
			totalDistance=totalDistance+Math.sqrt(((y2-y1)*(y2-y1))+((x2-x1)*(x2-x1))); //calculate the euclidean distance between each pair of consecutive points
		                          //calculate mean distance
		}
		meandistance=totalDistance/((16));
		//System.out.println("meandistance="+meandistance);
		finalVector.add(pathscale[0]);
		finalVector.add(pathscale[1]);
		int b4index=0,tempIndex=0;                        //the ist point 
		for(int index=1;index<((numPoints/2)-1);index++)   //index is always the second point
		{
			
			float y2=pathscale[2*index+1];
			float y1=pathscale[2*tempIndex+1];
			float x2=pathscale[2*index];
			float x1=pathscale[2*tempIndex];
			distance=distance+Math.sqrt(((y2-y1)*(y2-y1))+((x2-x1)*(x2-x1)));   //distance between consecutive points
			if(distance>meandistance)
			{
				b4index=index;
				tempIndex=b4index; 
				distance=0;
				finalVector.add(x2);
				finalVector.add(y2);
				
			}
			else
			{
				tempIndex=index;
			}
			
		}
		System.out.println(numPoints/2+":"+finalVector.size()/2);
		float[] finalvals=new float[finalVector.size()];
		for(int i=0;i<finalVector.size();i++)
		{
			finalvals[i]=finalVector.get(i);
		}
		
		return finalvals;
	}
	
	*/
	
	public void scale() //scaling function to scale_down the bounding box of the shape
	{
		
		//System.out.println(weight+":"+height_dflt);
		
		
		double width, height, DiffW, DiffH;
		double sWth=width_dflt,sHth=height_dflt;
		  float max_x = 0, max_y = 0, min_x=0,min_y=0;
		  int i;
		 //double scale;
		  
		  int numcount=count;
		  for(int j=0;j<numcount;j++)
		  {
			  if(_drawn.get(2*j)>max_x)
				  max_x=_drawn.get(2*j);
			  if(_drawn.get(2*j+1)>max_y)
				  max_y=_drawn.get(2*j+1);
		  }
		  min_x=max_x;
		  min_y=max_y;
		 // System.out.println("Min_x="+min_x+","+"Min_y="+min_y);
		  pathscale=new float[numcount*2];
		  for(i=0; i<numcount; i++)
		  {
			
				
			if(_drawn.get(2*i)< min_x )
				min_x =_drawn.get(2*i);
				
							
			if(_drawn.get(2*i+1) < min_y )
				min_y = _drawn.get(2*i+1);
				
		  }
		  
			width = max_x - min_x;
			height = max_y - min_y; 
			//System.out.println("width="+width+","+"height="+height);
			
			if(width<=sWth&& width >=0.5*sWth&&height<=sHth&&height >=0.5*sHth) //then do only translation
			{
			
				for(i=0; i<numcount; i++) 
				{
					pathscale[i*2]=_drawn.get(2*i)-(float)min_x;
					//System.out.println("x:"+pathScale[i*2]);
					pathscale[i*2+1]=_drawn.get(2*i+1)-(float)min_y;
					//System.out.println("y:"+pathScale[i*2+1]);
				}
				 
				
			}
			else if(width>sWth||height>sHth) 
			{
				if(width<=sWth)
				{
					scale=height/sHth;
				}
				else if(height<=sHth)
				{
					scale=width/sWth;
				}
				else
				{
					DiffW = Math.abs(sWth - width);
					DiffH = Math.abs(sHth - height);
					//System.out.println("width="+DiffW+","+"height="+DiffH);
					if( DiffW > DiffH )
						scale = width/sWth;
					else
						scale = height/sHth;
				}
					
				
				width=width/scale;
				height=height/scale;
					
				//scaling of the shape
				//System.out.println("scale:"+scale);
				for(i=0; i<numcount; i++)
				{
					pathscale[i*2] =_drawn.get(2*i);
					pathscale[i*2]=(float)(pathscale[i*2]/scale);
					pathscale[i*2+1] =_drawn.get(2*i+1);
					pathscale[i*2+1]=(float)(pathscale[i*2+1]/scale);
				}
				
				
				
				
				double diff_x = min_x/scale;
				double diff_y = min_y/scale;
				
				
			
				
				for(i=0; i<numcount; i++) //final scaling of the shape(center adjustment)
				{
					pathscale[i*2]=pathscale[i*2]-(float)diff_x;
					//System.out.println("x:"+pathScale[i*2]);
					pathscale[i*2+1]=pathscale[i*2+1]-(float)diff_y;
					//System.out.println("y:"+pathScale[i*2+1]);
				}
				 
			
			}
			else
			{
				
				
					DiffW = Math.abs(sWth - width);
					DiffH = Math.abs(sHth - height);
					//System.out.println("width="+DiffW+","+"height="+DiffH);
					if( DiffW < DiffH )
						scale = sWth/width;
					else
						scale = sHth/height;
								
				
				width=width*scale;
				height=height*scale;
					
				//scaling of the shape
				//System.out.println("scale:"+scale);
				for(i=0; i<numcount; i++)
				{
					pathscale[i*2] =_drawn.get(2*i);
					pathscale[i*2]=(float)(pathscale[i*2]*scale);
					pathscale[i*2+1] =_drawn.get(2*i+1);
					pathscale[i*2+1]=(float)(pathscale[i*2+1]*scale);
				}
				
				
				
				double diff_x = min_x*scale;
				double diff_y = min_y*scale;
				
				
			
				
				for(i=0; i<numcount; i++) //final scaling of the shape(center adjustment)
				{
					pathscale[i*2]=pathscale[i*2]-(float)diff_x;
					//System.out.println("x:"+pathScale[i*2]);
					pathscale[i*2+1]=pathscale[i*2+1]-(float)diff_y;
					//System.out.println("y:"+pathScale[i*2+1]);
				}
				 
			
			}
	}
			
						
						
	public float[] temporalsampling(float totaldistance,float[] substrokes,int number)
	{
		/*
		float[] equi=new float[2*number];
		int n=number;
		double meandistance=totaldistance/(number-1);
		double distance=0,lastdistance=0;
		double x=substrokes[0];
		double y=substrokes[1];
		equi[0]=(float)x;
		equi[1]=(float)y;
		int k=1;
		for(int index=1;index<((substrokes.length/2)-2);index++)   //index is always the second point
		{
			
			float py2=pathscale[2*index+3];
			float px2=pathscale[2*index+2];
			float py1=pathscale[2*index+1];
			float px1=pathscale[2*index];
			if(distance==0 ){
				px1=(float) x;
				py1=(float) y;
			}
			lastdistance=distance;
			distance+=calculateDist(px1, px2, py1, py2);   //distance between consecutive points
			double mnext=distance%meandistance;
			double mprev=lastdistance%meandistance;
			
			if(mnext<=mprev)
			{
				
				double m1=distance-meandistance;
				double n1=meandistance-lastdistance;
				
				x=(m1*px1+n1*px2)/(m1+n1);
				y=(m1*py1+n1*py2)/(m1+n1);
				k++;
				
				if(k==n)
					break;
				equi[2*k]=(float) x;
				equi[2*k+1]=(float) y;
				distance=0;
				index--;
				
			}
			for(index=(substrokes.length/2-1);index>=0;index--)  //finding the last filled element in the equi array
			{
				if((equi[2*index]!=0f)||equi[2*index+1]!=0f)
					break;
			}
			if(index==-1)
				return equi;
			for(;index<substrokes.length/2;index++)   //fill all last empty elements by last point
			{
				if(equi[2*index]==0f && equi[2*index+1]==0f)
				{
					equi[2*index]=substrokes.length-2;
					equi[2*index+1]=substrokes.length-1;
				}
			}
			
			
		}
	
		return equi;
		
	*/
	
	
	
	
	
										final float increment = (totaldistance) / (number - 1);
										int vectorLength = number*2;
								        float[] vector = new float[vectorLength];
								        float distanceSoFar = 0;
								        float[] pts = substrokes;
								        float lstPointX = pts[0];
								        float lstPointY = pts[1];
								        int index = 0;
								        float currentPointX = Float.MIN_VALUE;
								        float currentPointY = Float.MIN_VALUE;
								        vector[index] = lstPointX;
								        index++;
								        vector[index] = lstPointY;
								        index++;
								        int i = 0;
								        int count = (pts.length / 2);
								        while ((i < count) ) {
								        	if (currentPointX == Float.MIN_VALUE) {
								        		i++;
								        		if (i >= count) {
								        			break;
								        		}
								        		currentPointX = pts[i * 2];
								        		currentPointY = pts[(i * 2) + 1];
								        	}
								        	float deltaX = currentPointX - lstPointX;
								        	float deltaY = currentPointY - lstPointY;
								        	float distance = (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
								        	if ((distanceSoFar + distance) >= increment) {
								        		float ratio = (increment - distanceSoFar) / distance;
								        		float nx = lstPointX + (ratio * deltaX);
								        		float ny = lstPointY + (ratio * deltaY);
								        		vector[index] = nx;
								        		index++;
								        		vector[index] = ny;
								        		index++;
								        		lstPointX = nx;
								        		lstPointY = ny;
								        		distanceSoFar = 0;
								        	} else {
								        		lstPointX = currentPointX;
								        		lstPointY = currentPointY;
								        		currentPointX = Float.MIN_VALUE;
								        		currentPointY = Float.MIN_VALUE;
								        		distanceSoFar += distance;
								        	}
								        }

								        for (i = index; i < (vectorLength); i += 2) {
								        	vector[i] = lstPointX;
								        	vector[i + 1] = lstPointY;
								        }
								        return vector;
								        
							}
				
class getstrokes{
	int size;
	public void setsize(int sizelist)
	{
		size=sizelist;
	}
	
	public int getsize()
	{
		
		return size;
	}

   public float[] strokes(float[] pathscale) throws Exception
   {
	   for(int z=0;z<pathscale.length/2;z++)
		{
			//System.out.println(pathscale[2*z]+","+pathscale[2*z+1]);
		}
	   list=new ArrayList<temp>();
	   
	   double PI=22/7;
	
	int i=0,j=1,N=(pathscale.length)/2;
	//System.out.println(pathscale.length/2);
	float[] xcoord=new float[(pathscale.length)/2];
	float[] ycoord=new float[(pathscale.length)/2];
	ArrayList<Double> arl=new ArrayList<Double>();
	for(int k=0;k<(pathscale.length)/2;k++)
	{
		xcoord[k]=pathscale[2*k];
		ycoord[k]=pathscale[2*k+1];
	}
	/*while(j<N)
	{
	double theta=(Math.atan2((ycoord[j]-ycoord[i]), (xcoord[j]-xcoord[i])))*(180/PI);
	segment: 
		while(j<N)
	{
			
		if(j==1)
		{
			j=j+1;
			continue;
		}
		double alpha=(Math.atan2((ycoord[j]-ycoord[i]), (xcoord[j]-xcoord[i])))*(180/PI);
		double beta=(Math.atan2((ycoord[j]-ycoord[j-1]),(xcoord[j]-xcoord[j-1])))*(180/PI);//betaj
		double beta1=(Math.atan2((ycoord[j-1]-ycoord[j-2]),(xcoord[j-1]-xcoord[j-2])))*(180/PI);//betaj-1
		if((Math.min((Math.abs(theta-alpha)), 360-Math.abs(theta-alpha)))<90 && (Math.min((Math.abs(beta1-beta)), (360-(Math.abs(beta1-beta))))<90))
		{
			
			
					j=j+1;
				
			
		}
		else
		{
		*/
	for(j=2;j<xcoord.length;j++)
	{
			float p1x=xcoord[j-2];//before critical point
			float p1y=ycoord[j-2];
			float p2x=xcoord[j-1];//critical point
			float p2y=ycoord[j-1];
			float p3x=xcoord[j];
			float p3y=ycoord[j];
			float A=(float) Math.sqrt((p2x-p1x)*(p2x-p1x)+(p2y-p1y)*(p2y-p1y));//distance between p2&p1
			float B=(float) Math.sqrt((p3x-p2x)*(p3x-p2x)+(p3y-p2y)*(p3y-p2y));
			float C=(float) Math.sqrt((p1x-p3x)*(p1x-p3x)+(p1y-p3y)*(p1y-p3y));
			double angle=Math.acos((A*A+B*B-C*C)/(2*A*B))*(180/PI);//compute acute angle at j-1 
			double greaterangle=360-angle;
			
			distribution((float) greaterangle);
			if(Math.max(angle,greaterangle)>220)
			{
				temp=new temp();
				temp.x=xcoord[j-1];
				temp.y=ycoord[j-1];
				temp.angle=Math.max(angle, greaterangle);
				list.add(temp);
				
			}
	}
			/*
			i=j;
			j=j+1;
			break segment;
		}
	}
	}
	*/
	
	setsize(arl.size());
	double[] breakpoints=new double[arl.size()];
	//for(int x=0;x<arl.size();x++)
	//{
		//breakpoints[x]= arl.get(x);
	//}
	//System.out.println("from method");
	
	//for(int k=0;k<list.size();k++)
	//{
	//System.out.println("x:"+list.get(k).x+","+"y:"+list.get(k).y+"angle:"+list.get(k).angle);
	//}
	
	
	float[] temp1 = null;
	if(list.size()==0)
	{
		System.out.println(1);
		return pathscale;
	}
		
	else if(list.size()==1)
	{
		temp1=onePtCrticalAngleShift(list.get(0).x, list.get(0).y);
		System.out.println(1);
	}
	
	else
	{
		System.out.println(2);
		temp max1=new temp();
		temp max2=new temp();
		double maxangle1=-360,maxangle2=-360;
		int pos1=0;
		int pos2=0;
		for(int tp=0;tp<list.size();tp++)
		{
			if(list.get(tp).angle>maxangle1)
			{
				maxangle1=list.get(tp).angle;
				max1=list.get(tp);
				pos1=tp;
					
			}
		}
		for(int tp=0;tp<list.size();tp++)
		{
			if((list.get(tp).angle!=maxangle1) && (list.get(tp).angle>maxangle2))
			{
				maxangle2=list.get(tp).angle;
				max2=list.get(tp);
				pos2=tp;
			}
		}
		if(pos1<pos2)		
			temp1=twoPointAngleShift(max1.x, max1.y,max2.x, max2.y);
		else
			temp1=twoPointAngleShift(max2.x, max2.y,max1.x, max1.y);
	}
		
		return temp1;
		//temp=onePtCrticalAngleShift(list.get(0).x, list.get(0).y);
	//return temp;
	
	
}
   

}
public void distribution(float angle)
{
	if(angle>180 && angle<=210)
		angleRange[0]++;
	else if(angle>210 && angle<=240)
		angleRange[1]++;
	else if(angle>240 && angle<=270)
		angleRange[2]++;
	else if(angle>270 && angle<=300)
		angleRange[3]++;
	else if(angle>300 && angle<=330)
		angleRange[4]++;
	else
		if(angle>330 && angle<=360)
			angleRange[5]++;
}
public float[] equidistant(String direction,float x1, float y1, float x2, float y2, int n,float meandistance ) throws Exception{
	float[] equi=new float[2*n]; //matrix to find the final vector
	double lastdistance=0,distance=0;
	
	int noofpts=0,i=0;//in pathscale between end points of (x1,y1) and (x2,y2)
	//Calculating starting point
	
	while(pathscale[2*i]!=x1||pathscale[2*i+1]!=y1){
		//System.out.println(pathscale[2*i]+","+pathscale[2*i+1]);
		i++;
		
	}
	int j=i;
	for(;i<pathscale.length/2;i++){
		noofpts++;
		if(pathscale[2*i]==x2&&pathscale[2*i+1]==y2){
			break;
		}
	}
	if(direction=="f"){
		double x=x1,y=y1;
		equi[0]=x1;
		equi[1]=y1;
		if(n==1)
			return equi;
		int k=0;
		for(int index=j;index<((pathscale.length/2)-1);index++)   //index is always the second point
		{
			
			float py2=pathscale[2*index+3];
			float px2=pathscale[2*index+2];
			float py1=pathscale[2*index+1];
			float px1=pathscale[2*index];
			if(distance==0 ){
				px1=(float) x;
				py1=(float) y;
			}
			lastdistance=distance;
			distance+=calculateDist(px1, px2, py1, py2);   //distance between consecutive points
			double mnext=distance%meandistance;
			double mprev=lastdistance%meandistance;
			
			if(mnext<=mprev)
			{
				
				double m1=distance-meandistance;
				double n1=meandistance-lastdistance;
				if(m1!=-n1)
				{
					x=(m1*px1+n1*px2)/(m1+n1);
					y=(m1*py1+n1*py2)/(m1+n1);
					k++;
					
					if(k==n)
						break;
					equi[2*k]=(float) x;
					equi[2*k+1]=(float) y;
					distance=0;
					index--;
				}
				
			}
			else
			{
				if((distance-lastdistance)>meandistance)
				{
					double m1=distance-meandistance;
					double n1=meandistance-lastdistance;
					x=(m1*px1+n1*px2)/(m1+n1);
					y=(m1*py1+n1*py2)/(m1+n1);
					k++;
					
					if(k==noofpts)
						break;
					equi[2*k]=(float) x;
					equi[2*k+1]=(float) y;
					distance=0;
					
					index--;
				}
			}
			
			
		}
		if((equi[equi.length-2]==0f) && (equi[equi.length-1]==0f))
		{
			double m1=meandistance;
			double n1=meandistance-distance;
			double px1=(float) x;
			double py1=(float) y;
			double py2=pathscale[pathscale.length-1];
			double px2=pathscale[pathscale.length-2];
			x=(n1*px1-m1*px2)/(n1-m1);
			y=(n1*py1-m1*py2)/(n1-m1);
			equi[equi.length-2]=(float) x;
			equi[equi.length-1]=(float) y;
		}
	}
	else{
		double x=x2,y=y2;
		//Saving Critical Point
		equi[2*n-1]=y2;
		equi[2*n-2]=x2;
		if(n==1)
			return equi;
		for(int index=noofpts-1;index>=1;index--)   //index is always the second point
		{
			
			float py1=pathscale[2*index+1];//lower point y coordinate
			float py2=pathscale[2*index-1];//higher point y coordinate
			float px1=pathscale[2*index];//lower point x coordinate
			float px2=pathscale[2*index-2];//higher point x coordinate
			if(distance==0 && index!=(noofpts-1)){
				px1=(float) x;
				py1=(float) y;
			}
			lastdistance=distance;
			distance+=calculateDist(px1, px2, py1, py2);   //distance between consecutive points
			
			double mnext=distance%meandistance;
			double mprev=lastdistance%meandistance;
			
			if(mnext<=mprev){
				// use section formula to between mnext and mprev to find the distance from (px1,py1)
				double m1=meandistance-lastdistance;
				double n1=distance-meandistance;
				if(m1!=-n1)
				{
					x=(m1*px2+n1*px1)/(m1+n1);
					y=(m1*py2+n1*py1)/(m1+n1);
					--n;
					if(n==0)
						break;
					equi[2*n-1]=(float) y;
					equi[2*n-2]=(float) x;
					distance=0;
					index++;
				}
			}
			else
			{
				if((distance-lastdistance)>meandistance)
				{
					double m1=meandistance-lastdistance;
					double n1=distance-meandistance;
					x=(m1*px2+n1*px1)/(m1+n1);
					y=(m1*py2+n1*py1)/(m1+n1);
					--n;
					if(n==0)
						break;
					equi[2*n-1]=(float) y;
					equi[2*n-2]=(float) x;
					distance=0;
					index++;
				}
			}
			
		}
		if((equi[0]==0f) && (equi[1]==0f))
		{
			double m1=meandistance;
			double n1=meandistance-distance;
			double px1=(float) x;
			double py1=(float) y;
			double py2=pathscale[1];
			double px2=pathscale[0];
			x=(n1*px1-m1*px2)/(n1-m1);
			y=(n1*py1-m1*py2)/(n1-m1);
			equi[0]=(float) x;
			equi[1]=(float) y;
		}
	}
	return equi;
}
public float[] onePtCrticalAngleShift(float x, float y) throws Exception{
	for(int z=0;z<pathscale.length/2;z++)
	{
		//System.out.println(pathscale[2*z]+","+pathscale[2*z+1]);
	}
		int numPoints=pathscale.length;
		float strokeTotalDistance=0;
		float strokePartialDistance=0;
		//float[] stroke1sthalf;
		ArrayList<Float> finalVector=new ArrayList<Float>();
		for(int pt=3;pt<=(numPoints-1);pt+=2)
		{
			float y2=pathscale[pt];
			float y1=pathscale[pt-2];
			float x2=pathscale[pt-1];
			float x1=pathscale[pt-3];
			strokeTotalDistance += calculateDist(x1, x2, y1, y2); //caluculate total stroke distance
		}
		for(int pt=3;pt<=(numPoints-1);pt+=2)
		{
			float y2=pathscale[pt];
			float y1=pathscale[pt-2];
			float x2=pathscale[pt-1];
			float x1=pathscale[pt-3];
			if(x2==x&&y2==y){
				strokePartialDistance += calculateDist(x1, x2, y1, y2);
				break;
			}
			strokePartialDistance += calculateDist(x1, x2, y1, y2); //caluculate stroke distance upto critical point
		}
		//System.out.println(strokeTotalDistance+","+strokePartialDistance);
		float ratio1=strokePartialDistance/strokeTotalDistance;
		//System.out.println(ratio1);
		int noofptsuptocp=(int) Math.ceil(ratio1*16);//no of pts upto critical pts.
		float meandistance=strokeTotalDistance/16;
		//System.out.println(noofptsuptocp+","+(16-noofptsuptocp));
		float a[]=equidistant("r", pathscale[0],pathscale[1],x,y,noofptsuptocp, meandistance);
		float b[]=equidistant("f",x,y,pathscale[pathscale.length-2],pathscale[pathscale.length-1],(17-noofptsuptocp), meandistance);
		float c[]=new float[32];
		for(int i=0;i<a.length;i++)
		{
			c[i]=a[i];
		}
		for(int k=1;k<b.length/2;k++)
		{
			if((a.length-2+2*k)==32)
				break;
			//System.out.println(a.length-2+2*k);
			c[a.length-2+(2*k)]=b[2*k];
			c[a.length-2+(2*k)+1]=b[2*k+1];
		}
			//equidistant("r",pathscale);
		/*
		if(x==pathscale[2*noofptsuptocp]&&y==pathscale[2*noofptsuptocp+1]){
			System.out.print("OK");
		}
		else
			System.out.print("NOT OK");
		float meanDistance=strokePartialDistance/noofptsuptocp;
		*/
		/*
		  stroke1sthalf=new float[noofptsuptocp*2];
		 
		for(int i=0;i<noofptsuptocp;i++){
			
		}
		*/
		return c;
	}

public float[] twoPointAngleShift(float x1,float y1,float x2,float y2) throws Exception
{
	
	double meandistance=0;
	double totalDistance=0;
	float[] centreStrokepts;
	ArrayList<Float> centreStroke=new ArrayList<Float>();
	int i=0,noofpts=0;
	int n=0,m=0,o=0;
	int secondcpstart;
	
	double distanceUptoIstCriticalpt=0;
	double distanceUpto2ndCriticalpt=0;
	
	
	while((pathscale[2*i]!=x1)||(pathscale[2*i+1]!=y1)){           // loop upto first critical point
		//System.out.println(pathscale[2*i]+","+pathscale[2*i+1]);
		i++;
		
	}
	--i;
	//System.out.println("__________________");
	while(true){    //loop upto second critical point and count the number of points(noofpts)
		++i;
		noofpts++;
		centreStroke.add(pathscale[2*i]);
		centreStroke.add(pathscale[2*i+1]);
		//System.out.println(pathscale[2*i]+","+pathscale[2*i+1]);
		
		
		if((pathscale[2*i]==x2)&&(pathscale[2*i+1]==y2))
		{
			secondcpstart=i;
			break;
		}
		
	}
	
	for(int pt=3;pt<=(pathscale.length-1);pt+=2)
	{
			float py2=pathscale[pt];
			float py1=pathscale[pt-2];
			float px2=pathscale[pt-1];
			float px1=pathscale[pt-3];
			totalDistance += calculateDist(px1, px2, py1, py2); //calculate total stroke distance
			if((py2==y1) && (px2==x1)) 							
				distanceUptoIstCriticalpt=totalDistance;		//calculate distance upto first critical point
			if((py2==y2) && (px2==x2))
				distanceUpto2ndCriticalpt=totalDistance-distanceUptoIstCriticalpt;					///calculate distance upto second critical point
	}
	meandistance=totalDistance/15; //calculate mean distance
	//System.out.println(meandistance);
	n=(int)Math.ceil((distanceUptoIstCriticalpt/totalDistance)*16);
	m=(int)Math.ceil((distanceUpto2ndCriticalpt/totalDistance)*16);
	//System.out.println(n+m);
	o=(18-n-m);
	 //obtain number of maximum points
	//System.out.println(n+":"+m+":"+o);
	
	centreStrokepts=new float[centreStroke.size()]; //fill the centreStroke array
	for(int indx=0;indx<centreStroke.size();indx++)
	{
		centreStrokepts[indx]=centreStroke.get(indx);
	}
	float prevX=centreStrokepts[centreStrokepts.length-2]; //last points i.e the second critical point
	float prevY=centreStrokepts[centreStrokepts.length-1];
	
	centreStrokepts=computecentreStroke(centreStrokepts,meandistance,m,distanceUpto2ndCriticalpt); //obtain the final centreStrokeArray
	
	float Xshift=centreStrokepts[centreStrokepts.length-2]-prevX; //Obtain the shift in the critical point
	float Yshift=centreStrokepts[centreStrokepts.length-1]-prevY;
	
	
	//Shift all the points for the forward trace[including the second critical point]
	for(int k=secondcpstart;k<((pathscale.length/2)-1);k++)
	{
		pathscale[2*k]=pathscale[2*k]+Xshift;
		pathscale[2*k+1]=pathscale[2*k+1]+Yshift;
	}
	
	
	//obtain the first array
	float a[]=equidistant("r",pathscale[0],pathscale[1], x1, y1,n, (float)meandistance); //first n points for the first reverse trace before the first critical point
	float b[]=centreStrokepts; //centre coordinates
	float c[]=equidistant("f",x2+Xshift,y2+Yshift,pathscale[pathscale.length-2],pathscale[pathscale.length-1],o, (float)meandistance); // last m points for last reverse trace after the second critical point
	
	//System.out.println(a.length/2+","+b.length/2+","+c.length/2);
	
	//compute the final array
	float[] finalarr=new float[32];
	for(int t=0;t<(a.length-2);t++)
	{
		finalarr[t]=a[t];
	}
	
	for(int t=0;t<(b.length-2);t++)
	{
		finalarr[a.length+t-2]=b[t];
	}
	for(int t=0;t<(c.length);t++)
	{
		finalarr[a.length+b.length+t-4]=c[t];
	}
	
	return finalarr;
}

public float[] computecentreStroke(float[] centreStroke,double meandistance,int noofpts,double totalDistance)
{
	
	float[] equi=new float[2*noofpts];
	double distance=0,lastdistance=0;
	double x=centreStroke[0],y=centreStroke[1];
	equi[0]=(float) x;
	equi[1]=(float) y;
	int k=0;
	int storeindex=0;
	if(noofpts==1)
		return equi;
	for(int index=0;index<((centreStroke.length/2)-1);index++)   //index is always the second point
	{
		/*for(int l=0;l<(centreStroke.length/2);l++)
		{
			System.out.println(centreStroke[2*l]+":"+centreStroke[2*l+1]);
		}*/
		float py2=centreStroke[2*index+3];
		float px2=centreStroke[2*index+2];
		float py1=centreStroke[2*index+1];
		float px1=centreStroke[2*index];
		if(distance==0 ){
			px1=(float) x;
			py1=(float) y;
		}
		lastdistance=distance;
		distance+=calculateDist(px1, px2, py1, py2);   //distance between consecutive points
		double mnext=distance%meandistance;
		double mprev=lastdistance%meandistance;
		int z=k;
		
		if(mnext<=mprev)
		{
			
			double m1=distance-meandistance;
			double n1=meandistance-lastdistance;
			if(m1!=-n1)
			{
				x=(m1*px1+n1*px2)/(m1+n1);
				y=(m1*py1+n1*py2)/(m1+n1);
				k++;
			
				if(k==noofpts)
					break;
				equi[2*k]=(float) x;
				equi[2*k+1]=(float) y;
				distance=0;
			
				index--;
			}
			
		}
		else
		{
			if((distance-lastdistance)>meandistance)
			{
				double m1=distance-meandistance;
				double n1=meandistance-lastdistance;
				x=(m1*px1+n1*px2)/(m1+n1);
				y=(m1*py1+n1*py2)/(m1+n1);
				k++;
				
				if(k==noofpts)
					break;
				equi[2*k]=(float) x;
				equi[2*k+1]=(float) y;
				distance=0;
				
				index--;
			}
		}
	}
	if((equi[equi.length-2]==0f) && (equi[equi.length-1]==0f))
	{
		double m1=meandistance;
		double n1=meandistance-distance;
		double px1=(float) x;
		double py1=(float) y;
		double py2=centreStroke[centreStroke.length-1];
		double px2=centreStroke[centreStroke.length-2];
		x=(n1*px1-m1*px2)/(n1-m1);
		y=(n1*py1-m1*py2)/(n1-m1);
		equi[equi.length-2]=(float) x;
		equi[equi.length-1]=(float) y;
	}
		
	
	/*
	float[] temp;
	float distance=0;
	float lastdistance=0;
	double x=0,y=0;
	
	temp=new float[2*noofpts];
	temp[0]=centreStroke[0];    //fill the first point
	temp[1]=centreStroke[1];
	int k=0;
	int storeindex=0;
	
	for(int index=0;index<((centreStroke.length/2)-1);index++) 
		{
			
			float py2=pathscale[2*index+3];
			float px2=pathscale[2*index+2];
			float py1=pathscale[2*index+1];
			float px1=pathscale[2*index];
			if(distance==0 && index!=0){
				px1=(float) x;
				py1=(float) y;
			}
			lastdistance=distance;
			distance+=calculateDist(px1, px2, py1, py2);   //distance between consecutive points
			double mnext=distance%meandistance;
			double mprev=lastdistance%meandistance;
			
			if(mnext<=mprev)
			{
				
				double m1=distance-meandistance;
				double n1=meandistance-lastdistance;
				
				
				x=(m1*px1+n1*px2)/(m1+n1);
				y=(m1*py1+n1*py2)/(m1+n1);
				k++;
				
				if(k==noofpts)
					break;
				temp[2*k]=(float) x;
				temp[2*k+1]=(float) y;
				distance=0;
				storeindex=index;
				index--;
				
			}
		}
		
		if(equi[(2*noofpts)-1]==0f && equi[2*noofpts-2]==0f)
		{//if last point of temp is empty then we consider 
			double ntdistance=calculateDist(equi[(2*noofpts)-4],centreStroke[2*storeindex+2],equi[(2*noofpts)-3],centreStroke[2*storeindex+3]); //
			double m1=meandistance;
			double n1=meandistance-(ntdistance);
			float px1=equi[(2*noofpts)-4];     //second last point
			float py1=equi[(2*noofpts)-3];
			
			float px2=centreStroke[2*storeindex+2]; //last point
			float py2=centreStroke[2*storeindex+3];
		
			if(k<noofpts/2)
			{
			
				equi[2*k]=(float) ((m1*px2-(n1*px1))/(m1-n1));
				equi[2*k+1]=(float) ((m1*py2-(n1*py1))/(m1-n1));
			}
			
		}
		System.out.println("----------------");
		for(int l=0;l<(equi.length/2);l++)
		{
			System.out.println(equi[2*l]+":"+equi[2*l+1]);
		}
		*/
	return equi;
	
	}


			//centering
			/*
			double finalheight=(sHth/2)-(height/2);
			double finalwidth=(sWth/2)-(width/2);
			for(i=0;i<numcount;i++)
			{
				pathscale[i*2]=pathscale[i*2]+(float)finalwidth;
			
				pathscale[i*2+1]=pathscale[i*2+1]+(float)finalheight;
				
			}*/
			
			//invalidate();

public float[] threePtSmoothing(float[] arr)// three point moving average(for smoothing)
{ 
	float[] a=new float[arr.length];
	a[0]=arr[0];
	a[1]=arr[1];
	for(int i=1;i<((arr.length/2)-1);i++){
		a[2*i]=(arr[2*i-2]+arr[2*i]+arr[2*i+2])/3;
		a[2*i+1]=(arr[2*i-1]+arr[2*i+1]+arr[2*i+3])/3;
	}
	a[arr.length-2]=arr[arr.length-2];
	a[arr.length-1]=arr[arr.length-1];
	return a;
}

public float[] smoothFunction() //
{
	float[] newPts=threePtSmoothing(pathscale); //smooth for the first time
	float strokeTotalDistance=0;
	float ratio=0.5f;  //predefined ratio
	for(int pt=3;pt<=(newPts.length-1);pt+=2)
	{
		float y2=newPts[pt];
		float y1=newPts[pt-2];
		float x2=newPts[pt-1];
		float x1=newPts[pt-3];
		strokeTotalDistance += calculateDist(x1, x2, y1, y2); //calculate total stroke distance
	}
	int noofPts=(int) Math.ceil(ratio*strokeTotalDistance); //total number of points in the  stroke
	float[] newpts1=temporalsampling(strokeTotalDistance,newPts,32); //construct equidistant points 
	float[] finalPts=threePtSmoothing(newpts1);
	float[] points32=temporalsampling(strokeTotalDistance,finalPts, 32);//smooth for the second time
		return points32;
	
}
//overridden method to pass the number of points 
public float[] smoothFunction(int noofpnts) //
{
	float[] newPts=threePtSmoothing(pathscale); //smooth for the first time
	float strokeTotalDistance=0;
	float ratio=0.5f;  //predefined ratio
	for(int pt=3;pt<=(newPts.length-1);pt+=2)
	{
		float y2=newPts[pt];
		float y1=newPts[pt-2];
		float x2=newPts[pt-1];
		float x1=newPts[pt-3];
		strokeTotalDistance += calculateDist(x1, x2, y1, y2); //calculate total stroke distance
	}
	int noofPts=(int) Math.ceil(ratio*strokeTotalDistance); //total number of points in the  stroke
	float[] newpts1=temporalsampling(strokeTotalDistance,newPts,noofpnts); //construct equidistant points 
	float[] finalPts=threePtSmoothing(newpts1);
	float[] points32=temporalsampling(strokeTotalDistance,finalPts, noofpnts);//smooth for the second time
		return points32;
	
}
}
			

	

