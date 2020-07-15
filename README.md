# FlexHMiner : discovering hierarchical process models

This project contains the code and examples of results for FlexHMiner. The approach is implemented as a ProM-plugin. A minimal example is provided in 
- FlexHMiner/example
- FlexHMiner/src/org/procecessmining/aam/Main.java (to run the example)


The details of the approach and its evaluation can be found in: 

Xixi Lu, Avigdor Gal, Hajo A. Reijers (2020). Discovering Hierarchical Processes Using Flexible Activity Trees for Event Abstraction.



## Result - Example BPIC12 log

Applying the FlexHMiner that uses the Inductive Miner and the Domain Knowledge based activity tree on the BPIC12 log, we obtain the following hierarhical process model.

We have the root model: 
![Root Model \label{mylabel}](./FlexHMiner/example/root_IMf_Model.png)

And three subprocesses A, O, and W:

![Root Model \label{mylabel}](./FlexHMiner/example/A_IMf_M.svg)
![Root Model \label{mylabel}](./FlexHMiner/example/O_IMf_M.svg)
![Root Model \label{mylabel}](./FlexHMiner/example/W_IMf_M.svg)


July 15, 2020
