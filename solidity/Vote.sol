pragma solidity >=0.4.18 <0.7.0;



contract Vote{

    uint budget_number=0;
    uint voter_count=0;
    uint total_vote=0;

    
    
    
    struct Student{
        bytes32 name;
        bytes32 student_id;
        address st_addr;
        bool vote;
        bool already_count;
        bool already_vote;
        
    }
    
    struct Budget{
        uint budget_number;
        
        bytes32 date; 
        bytes32 state;  
      
        uint agree;
        uint disagree;

    }

    mapping(uint => Budget) public budgets;
    mapping (address => Student) public voters;
    mapping (uint => address) public voters_list;
    
    event Result(uint _agree, uint _disagree,uint _total_vote);
    
    
    function _initBudget(bytes32 _date) public{
    
        budgets[budget_number] = Budget(budget_number,_date,"in progress",0,0);
    }


    function _voteToBudget(bytes32 _name, bytes32 student_id, bool vote_option) public{
        
        require(voters[msg.sender].name != _name && voters[msg.sender].already_vote == false);
        
        voters[msg.sender] = Student(_name,student_id,msg.sender,vote_option,false,true);
        voters_list[voter_count] = msg.sender;
        voter_count++;
        total_vote++;

    }
    

    
    
    function _countVoted() public{
        
        for(uint i=0;i<voter_count;i++){
            require( voters[voters_list[i]].already_count == false);
	        voters[voters_list[i]].already_count = true;
            if(voters[voters_list[i]].vote == true){
                budgets[budget_number].agree++;
            }
            else{
                budgets[budget_number].disagree++;
            }
        }
    }
    
    
    function _getResult() public {

        require(budgets[budget_number].state == "in progress");
        Result(budgets[budget_number].agree,budgets[budget_number].disagree,total_vote);
        
        
        if(budgets[budget_number].agree > budgets[budget_number].disagree){
            budgets[budget_number].state = "passed";
        }
        else{
            budgets[budget_number].state = "drop";
        }
        
        for(uint i=0;i<total_vote;i++){
            delete voters[voters_list[i]];
            delete voters_list[i];
        }
        
        total_vote=0;
        voter_count=0;
        budget_number++;
        
    }
    


}