package com.LRG.Utils;

public enum Function {
	
	//Technical Functions
	SMA (FunctionType.TECHNICAL, "Simple Moving Average"),
	EMA (FunctionType.TECHNICAL,"Exponential Moving Average"),
	DEMA (FunctionType.TECHNICAL,"Double Exponential Moving Average"),
	KAMA (FunctionType.TECHNICAL,"Kaufman Adaptive Moving Average"),
	TEMA (FunctionType.TECHNICAL, "Triple Exponential Moving Average"),
	TRIMA (FunctionType.TECHNICAL, "Triangular Moving Average"),
	T3 (FunctionType.TECHNICAL),
	ROC (FunctionType.TECHNICAL,"Rate of change"),
	RSI (FunctionType.TECHNICAL,"Relative Strength Index"),
	MACD (FunctionType.TECHNICAL,"Moving Average Convergence/Divergence"),
	MFI (FunctionType.TECHNICAL,"Money Flow Index"),
	CCI (FunctionType.TECHNICAL,"Commodity Channel Index"),
	BBAND(FunctionType.TECHNICAL,"Bollinger Bands"),
	OBV(FunctionType.TECHNICAL,"On Balance Volume")
	;

	
	private String code;
	private FunctionType functionType;
	private String guiCode;
		
	Function (FunctionType functionType) {
		this.functionType = functionType;
	}

    Function (String code, FunctionType functionType) {
    	this.code = code;
        this.functionType = functionType;
    }
    
    Function (String code, FunctionType functionType, String guiCode) {
    	this.code = code;
        this.functionType = functionType;
        this.guiCode = guiCode;
    }
    
    Function (FunctionType functionType, String guiCode) {
    	this.functionType = functionType;
        this.guiCode = guiCode;
    }

    public FunctionType getFunctionType() {
    	return functionType;
    }
    
    public String getCode() {
    	return code;
    }
    
    public String getGuiCode() {
    	return guiCode;
    }
    
    public static Function getFunctionFromGuiCode(String guiCode) {
        for (Function  type : Function.values()) {
            if(type.guiCode != null && type.guiCode.equals(guiCode)) {
               	return type;
            }
        }
		return null;
    }

    public enum FunctionType {
		TECHNICAL,FINANCIAL,MARKET,BENCHMARK,STATISTICS,ECONOMIC_SMA,ECONOMIC ;
	}
    
    
};
