pragma solidity ^0.4.18;


contract Vote_Executions{

    uint execution_number=0;
    uint voter_count=0;
    uint total_vote=0;

    
    
    
    struct Observer{
        bytes32 name;
        bytes32 student_id;
        address st_addr;
        bool vote;
        bool already_count;
        bool already_vote;
        
    }
    
    struct Execution{
        uint execution_number;
        
        bytes32 date; 
        bytes32 state;  
      
        uint agree;
        uint disagree;

    }

    mapping(uint => Execution) public executions;
    mapping (address => Observer) public voters;
    mapping (uint => address) public voters_list;
    
    event Result(uint _agree, uint _disagree,uint _total_vote);
   
    
    function _initExecution(bytes32 _date) public{
    
        executions[execution_number] = Execution(execution_number,_date,"in progress",0,0);
    }


    function _voteToExecution(bytes32 _name, bytes32 student_id, bool vote_option) public{
        
        require(voters[msg.sender].name != _name && voters[msg.sender].already_vote == false);
        
        voters[msg.sender] = Observer(_name,student_id,msg.sender,vote_option,false,true);
        voters_list[voter_count] = msg.sender;
        voter_count++;
        total_vote++;

    }
    

    
    
    function _countVoted_Execution() public{
        
        for(uint i=0;i<voter_count;i++){
            require( voters[voters_list[i]].already_count == false);
           voters[voters_list[i]].already_count = true;
            if(voters[voters_list[i]].vote == true){
                executions[execution_number].agree++;
            }
            else{
                executions[execution_number].disagree++;
            }
        }
    }
    
    
    function _getResult_Execution() public  {

     
        Result(executions[execution_number].agree,executions[execution_number].disagree,total_vote);
        require(executions[execution_number].state == "in progress");
       
        if(executions[execution_number].agree > executions[execution_number].disagree){
            executions[execution_number].state = "passed";
        }
        else{
            executions[execution_number].state = "drop";
        }
        
        for(uint i=0;i<total_vote;i++){
            delete voters[voters_list[i]];
            delete voters_list[i];
        }
        
        total_vote=0;
        voter_count=0;
        execution_number++;
                      

}
   

}