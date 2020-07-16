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
public class Vote_Executions extends Contract {
    private static final String BINARY = "60606040526000805560006001556000600255341561001d57600080fd5b6108138061002c6000396000f3006060604052600436106100825763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166312acab3a81146100875780631a8274aa1461009f5780631ac9e17e146100b2578063a3ec138d146100c5578063ba0adc9e1461012b578063f76c92291461015d578063fe16c2f8146101a4575b600080fd5b341561009257600080fd5b61009d6004356101c2565b005b34156100aa57600080fd5b61009d61024a565b34156100bd57600080fd5b61009d61035b565b34156100d057600080fd5b6100e4600160a060020a036004351661052b565b6040519586526020860194909452600160a060020a039092166040808601919091529015156060850152901515608084015290151560a083015260c0909101905180910390f35b341561013657600080fd5b61014160043561059a565b604051600160a060020a03909116815260200160405180910390f35b341561016857600080fd5b6101736004356105b5565b60405194855260208501939093526040808501929092526060840152608083019190915260a0909101905180910390f35b34156101af57600080fd5b61009d60043560243560443515156105e5565b60a060405190810160409081526000805480845260208085018690527f696e2070726f677265737300000000000000000000000000000000000000000084860152606085018390526080850183905290825260039052208151815560208201516001820155604082015160028201556060820151816003015560808201516004909101555050565b60005b60015481101561035857600081815260056020908152604080832054600160a060020a03168352600490915290206002015460a860020a900460ff161561029357600080fd5b60008181526005602090815260408083208054600160a060020a03908116855260048085528386206002908101805475ff000000000000000000000000000000000000000000191660a860020a17905592549091168552909252909120015474010000000000000000000000000000000000000000900460ff16151560011415610336576000805481526003602081905260409091200180546001019055610350565b600080548152600360205260409020600401805460010190555b60010161024d565b50565b600080548152600360208190526040808320918201546004909201546002547f93a6d32e3308943db7958716809830759672c9f1f8aa3ca6ecf89278a74fc6b693925180848152602001838152602001828152602001935050505060405180910390a1600080548152600360205260409020600201547f696e2070726f6772657373000000000000000000000000000000000000000000146103fc57600080fd5b60008054815260036020819052604090912060048101549101541115610457576000805481526003602052604090207f706173736564000000000000000000000000000000000000000000000000000060029091015561048e565b6000805481526003602052604090207f64726f70000000000000000000000000000000000000000000000000000000006002909101555b5060005b6002548110156105175760008181526005602081815260408084208054600160a060020a031685526004835290842084815560018181018690556002909101805476ffffffffffffffffffffffffffffffffffffffffffffff1916905593859052919052805473ffffffffffffffffffffffffffffffffffffffff1916905501610492565b506000600281905560018181558154019055565b600460205260009081526040902080546001820154600290920154909190600160a060020a0381169060ff74010000000000000000000000000000000000000000820481169160a860020a81048216917601000000000000000000000000000000000000000000009091041686565b600560205260009081526040902054600160a060020a031681565b60036020819052600091825260409091208054600182015460028301549383015460049093015491939092909185565b600160a060020a0333166000908152600460205260409020548314801590610643575033600160a060020a0316600090815260046020526040902060020154760100000000000000000000000000000000000000000000900460ff16155b151561064e57600080fd5b60c060405190810160409081528482526020808301859052600160a060020a0333168284018190528415156060850152600060808501819052600160a08601529081526004909152208151815560208201516001820155604082015160028201805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03929092169190911790556060820151600282018054911515740100000000000000000000000000000000000000000274ff000000000000000000000000000000000000000019909216919091179055608082015160028201805491151560a860020a0275ff0000000000000000000000000000000000000000001990921691909117905560a08201516002918201805476ff00000000000000000000000000000000000000000000191676010000000000000000000000000000000000000000000092151592909202919091179055600180546000908152600560205260409020805473ffffffffffffffffffffffffffffffffffffffff191633600160a060020a03161790558054810181558154019055505050505600a165627a7a72305820e9db34720d32b5adfb24e7c4757081d87ae3eb774084dca0d48609a63976a8fa0029";

    protected Vote_Executions(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Vote_Executions(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
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

    public RemoteCall<TransactionReceipt> _initExecution(byte[] _date) {
        final Function function = new Function(
                "_initExecution",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_date)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> _countVoted_Execution() {
        final Function function = new Function(
                "_countVoted_Execution",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> _getResult_Execution() {
        final Function function = new Function(
                "_getResult_Execution",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<Tuple5<BigInteger, byte[], byte[], BigInteger, BigInteger>> executions(BigInteger param0) {
        final Function function = new Function("executions",
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

    public RemoteCall<TransactionReceipt> _voteToExecution(byte[] _name, byte[] student_id, Boolean vote_option) {
        final Function function = new Function(
                "_voteToExecution",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_name),
                        new org.web3j.abi.datatypes.generated.Bytes32(student_id),
                        new org.web3j.abi.datatypes.Bool(vote_option)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Vote_Executions> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Vote_Executions.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Vote_Executions> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Vote_Executions.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Vote_Executions load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vote_Executions(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Vote_Executions load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vote_Executions(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ResultEventResponse {
        public Log log;

        public BigInteger _agree;

        public BigInteger _disagree;

        public BigInteger _total_vote;
    }
}
