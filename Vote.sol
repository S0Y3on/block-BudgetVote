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
        
    }
    
    struct Budget{
        uint budget_number;
        
        bytes32 date;        // ex) 2020/05/1
        bytes32 state;    //in progress, passed, drop
      
        uint agree;          // count agree vote
        uint disagree;          // count disagree vote 

        
    }

    mapping(uint => Budget) public budgets;
    
    // voter["0x~"] = Student("kimminhyeok","201611997","0x~",false)
    mapping (address => Student) public voters;
    // voters_list[0] = "0x~~"
    mapping (uint => address) public voters_list;
    
    
    
    
    
    
    function _initBudget(bytes32 _date) public{
        
        
        budgets[budget_number] = Budget(budget_number,_date,"in progress",0,0);
        budget_number++;

    }
    
    function _getBudgetNumber() public view returns (uint){
        for(uint i=0;i<budget_number;i++){
            if(budgets[i].state == "in progress"){
                return i;
            }
        }
        return 0;
    }
    

    function _voteToBudget(bytes32 _name, bytes32 student_id, bool vote_option) public{
        
        require(voters[msg.sender].name != _name && voters[msg.sender].vote == false);
        
        voters[msg.sender] = Student(_name,student_id,msg.sender,vote_option,false);
        voters_list[voter_count] = msg.sender;
        voter_count++;
        total_vote++;


    }
    

    
    
    function _countVoted() public{
        uint id = _getBudgetNumber();
        for(uint i=0;i<voter_count;i++){
            require( voters[voters_list[i]].already_count == false);
            if(voters[voters_list[i]].vote == true){
                budgets[id].agree++;
            }
            else{
                budgets[id].disagree++;
            }
        }
    }
    
    
    function _getResult() public returns(uint,uint,uint){
        uint id = _getBudgetNumber();
        require(budgets[id].state == "in progress");
        
        if(budgets[id].agree > budgets[id].disagree){
            budgets[id].state = "passed";
        }
        else{
            budgets[id].state = "drop";
        }


        
        return (budgets[id].agree,budgets[id].disagree,total_vote);
    
    }
    


}