package com.example.capstone;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class Vote extends Contract {
    private static final String BINARY = "60606040526000805560006001556000600255341561001d57600080fd5b6108138061002c6000396000f3006060604052600436106100825763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631e9e5bde8114610087578063252682a41461009c5780634363bc26146100b25780635b3cd15f146100d057806369f0e05f146100e3578063a3ec138d1461012a578063ba0adc9e14610190575b600080fd5b341561009257600080fd5b61009a6101c2565b005b34156100a757600080fd5b61009a600435610392565b34156100bd57600080fd5b61009a600435602435604435151561041a565b34156100db57600080fd5b61009a61061c565b34156100ee57600080fd5b6100f960043561072d565b60405194855260208501939093526040808501929092526060840152608083019190915260a0909101905180910390f35b341561013557600080fd5b610149600160a060020a036004351661075d565b6040519586526020860194909452600160a060020a039092166040808601919091529015156060850152901515608084015290151560a083015260c0909101905180910390f35b341561019b57600080fd5b6101a66004356107cc565b604051600160a060020a03909116815260200160405180910390f35b600080548152600360205260408120600201547f696e2070726f67726573730000000000000000000000000000000000000000001461020057600080fd5b60008054815260036020819052604091829020908101546004909101546002547f93a6d32e3308943db7958716809830759672c9f1f8aa3ca6ecf89278a74fc6b6935180848152602001838152602001828152602001935050505060405180910390a1600080548152600360208190526040909120600481015491015411156102be576000805481526003602052604090207f70617373656400000000000000000000000000000000000000000000000000006002909101556102f5565b6000805481526003602052604090207f64726f70000000000000000000000000000000000000000000000000000000006002909101555b5060005b60025481101561037e5760008181526005602081815260408084208054600160a060020a031685526004835290842084815560018181018690556002909101805476ffffffffffffffffffffffffffffffffffffffffffffff1916905593859052919052805473ffffffffffffffffffffffffffffffffffffffff19169055016102f9565b506000600281905560018181558154019055565b60a060405190810160409081526000805480845260208085018690527f696e2070726f677265737300000000000000000000000000000000000000000084860152606085018390526080850183905290825260039052208151815560208201516001820155604082015160028201556060820151816003015560808201516004909101555050565b600160a060020a0333166000908152600460205260409020548314801590610478575033600160a060020a0316600090815260046020526040902060020154760100000000000000000000000000000000000000000000900460ff16155b151561048357600080fd5b60c060405190810160409081528482526020808301859052600160a060020a0333168284018190528415156060850152600060808501819052600160a08601529081526004909152208151815560208201516001820155604082015160028201805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03929092169190911790556060820151600282018054911515740100000000000000000000000000000000000000000274ff000000000000000000000000000000000000000019909216919091179055608082015160028201805491151560a860020a0275ff0000000000000000000000000000000000000000001990921691909117905560a08201516002918201805476ff00000000000000000000000000000000000000000000191676010000000000000000000000000000000000000000000092151592909202919091179055600180546000908152600560205260409020805473ffffffffffffffffffffffffffffffffffffffff191633600160a060020a0316179055805481018155815401905550505050565b60005b60015481101561072a57600081815260056020908152604080832054600160a060020a03168352600490915290206002015460a860020a900460ff161561066557600080fd5b60008181526005602090815260408083208054600160a060020a03908116855260048085528386206002908101805475ff000000000000000000000000000000000000000000191660a860020a17905592549091168552909252909120015474010000000000000000000000000000000000000000900460ff16151560011415610708576000805481526003602081905260409091200180546001019055610722565b600080548152600360205260409020600401805460010190555b60010161061f565b50565b60036020819052600091825260409091208054600182015460028301549383015460049093015491939092909185565b600460205260009081526040902080546001820154600290920154909190600160a060020a0381169060ff74010000000000000000000000000000000000000000820481169160a860020a81048216917601000000000000000000000000000000000000000000009091041686565b600560205260009081526040902054600160a060020a0316815600a165627a7a72305820879d30ed9636827e14326f6034b3041d9eb1541454accc1f22345968e4a9dbf70029";

    protected Vote(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Vote(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<ResultEventResponse> getResultEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Result", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ResultEventResponse> responses = new ArrayList<ResultEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ResultEventResponse typedResponse = new ResultEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._agree = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._disagree = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._total_vote = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ResultEventResponse> resultEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Result", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ResultEventResponse>() {
            @Override
            public ResultEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ResultEventResponse typedResponse = new ResultEventResponse();
                typedResponse.log = log;
                typedResponse._agree = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._disagree = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._total_vote = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> _getResult() {
        final Function function = new Function(
                "_getResult", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> _initBudget(byte[] _date) {
        final Function function = new Function(
                "_initBudget", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> _voteToBudget(byte[] _name, byte[] student_id, Boolean vote_option) {
        final Function function = new Function(
                "_voteToBudget", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_name), 
                new org.web3j.abi.datatypes.generated.Bytes32(student_id), 
                new org.web3j.abi.datatypes.Bool(vote_option)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> _countVoted() {
        final Function function = new Function(
                "_countVoted", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple5<BigInteger, byte[], byte[], BigInteger, BigInteger>> budgets(BigInteger param0) {
        final Function function = new Function("budgets", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple5<BigInteger, byte[], byte[], BigInteger, BigInteger>>(
                new Callable<Tuple5<BigInteger, byte[], byte[], BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, byte[], byte[], BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, byte[], byte[], BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<Tuple6<byte[], byte[], String, Boolean, Boolean, Boolean>> voters(String param0) {
        final Function function = new Function("voters", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple6<byte[], byte[], String, Boolean, Boolean, Boolean>>(
                new Callable<Tuple6<byte[], byte[], String, Boolean, Boolean, Boolean>>() {
                    @Override
                    public Tuple6<byte[], byte[], String, Boolean, Boolean, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<byte[], byte[], String, Boolean, Boolean, Boolean>(
                                (byte[]) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<String> voters_list(BigInteger param0) {
        final Function function = new Function("voters_list", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Vote> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Vote.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Vote> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Vote.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Vote load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vote(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Vote load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vote(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ResultEventResponse {
        public Log log;

        public BigInteger _agree;

        public BigInteger _disagree;

        public BigInteger _total_vote;
    }
}
