# GreedySearch

Greedy search solution for a vehicle routing problem with the following description:

## Optimization
Write at least two optimization programs that solve the below given vehicle routing problems. One of the optimization programs must use local search and the other one can use any optimization approach. All problems are minimization problems. 

## Vehicle Routing Problem Description
You are trying to optimize the cost of collecting garbage for a garbage disposal firm. You have three types of garbage to collect (organic, plastic and paper). 
Each truck can only collect one type of garbage on a single route. You have unlimited number of trucks and workers available. A truck must pick up garbage on a visited site if it has available capacity while travelling past the site. 
It also must pick everything from a given site and cannot partially empty a location. 
Each truck must return to the disposal facility at the end of a day. Some roads between towns are one way roads and some include bridges which have max carrying capacity.  
The cost of collection is composed of the fixed fee for each trip, the distance travelled by the truck and hourly pay for the employee. Each trip has a fixed cost of 10e. The distance travelled has cost 0.1e per km. 
The hourly cost for an employee is 10e per hour. The average speed of a truck is 50km/h and it takes on average 12 minutes to collect garbage on each site irrelevant of the amount of garbage. If the time of a route exceeds 8 hours the extra employeetime costs double. 
Time for unloading when the trucks returns to the firm is 30minutes.  

## Problem description 
Each problem is given in a separate file. Every value in the file is separated by comma. First line tells you the number of sites available and the maximum capacity of each truck. 
Site 1 is always the garbage disposal firm from which all the trucks start and finish.  
After the fist line descriptions of the sites are given. Each line includes the index of a site, x coordinate, y coordinate, amount of organic garbage to collect, amount of plastic garbage to collect and amount of paper garbage to collect in this order.  
After the site info road information is given. Each line represents the connection between two sites given with the fist two indexes, the third number is the distance between two sites. 
The fourth number tells you if the road is oneway road (0 is two-way road, 1 is one-way road going only from first index to second). The last value is the maximum capacity the road.
If the truck has more load than maximum road capacity it cannot travel over it. Also note that two sites can be connected by 1 or sometimes more roads. 
If possible you always choose the shortest road.  
 Example of a input file:   
    
<b>5,100  
1,2.375864,2.864949,0,0,0  
2,7.824403,3.614628,72.65013,11.8694,51.84442  
3,5.038958,6.564542,4.61077,12.90976,76.93038  
4,1.22205,6.255063,35.09712,41.66061,4.507426  
5,3.776397,8.558841,75.22943,46.65396,24.542  
4,2,8.973133,0,100  
1,3,5.395846,0,100  
1,2,6.829697,0,100  
1,5,5.907405,0,100  
2,3,4.486293,1,100  
2,5,8.208778,0,100  
5,2,6.617438,0,100  
2,1,6.610899,0,100  
1,4,4.218883,0,61.44958  
5,3,2.687808,1,100</b>

## Solution description  
In the report write each solution as:  

<b>1,1,4,1  
2,1,2,3,4,1  
3,1,4,2,1  
1,1,4,2,3,1</b>  
where the first number on each line is the index of the type of garbage to collect and the rest are the order of sites the truck visits. 
This example shows that we had four trucks. Two of them were collecting organic (1) garbage, one plastic (2) and one paper (3). 
We assume that the truck on line 1 collected garbage at site 4 and the truck on line 4 just passed by that site.
