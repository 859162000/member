
/**
 * ReceiveServerCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.wanda.ccs2.wcf;

    /**
     *  ReceiveServerCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ReceiveServerCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ReceiveServerCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ReceiveServerCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for setRTXJSON method
            * override this method for handling normal response from setRTXJSON operation
            */
           public void receiveResultsetRTXJSON(
                    com.wanda.ccs2.wcf.service.SetRTXJSONResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setRTXJSON operation
           */
            public void receiveErrorsetRTXJSON(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setRTXInfo method
            * override this method for handling normal response from setRTXInfo operation
            */
           public void receiveResultsetRTXInfo(
                    com.wanda.ccs2.wcf.service.SetRTXInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setRTXInfo operation
           */
            public void receiveErrorsetRTXInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getIP method
            * override this method for handling normal response from getIP operation
            */
           public void receiveResultgetIP(
                    com.wanda.ccs2.wcf.service.GetIPResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getIP operation
           */
            public void receiveErrorgetIP(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setEmailJSON method
            * override this method for handling normal response from setEmailJSON operation
            */
           public void receiveResultsetEmailJSON(
                    com.wanda.ccs2.wcf.service.SetEmailJSONResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setEmailJSON operation
           */
            public void receiveErrorsetEmailJSON(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setMMSInfo method
            * override this method for handling normal response from setMMSInfo operation
            */
           public void receiveResultsetMMSInfo(
                    com.wanda.ccs2.wcf.service.SetMMSInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setMMSInfo operation
           */
            public void receiveErrorsetMMSInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setSMSInfo method
            * override this method for handling normal response from setSMSInfo operation
            */
           public void receiveResultsetSMSInfo(
                    com.wanda.ccs2.wcf.service.SetSMSInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setSMSInfo operation
           */
            public void receiveErrorsetSMSInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setSMSJSON method
            * override this method for handling normal response from setSMSJSON operation
            */
           public void receiveResultsetSMSJSON(
                    com.wanda.ccs2.wcf.service.SetSMSJSONResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setSMSJSON operation
           */
            public void receiveErrorsetSMSJSON(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setEmailInfo method
            * override this method for handling normal response from setEmailInfo operation
            */
           public void receiveResultsetEmailInfo(
                    com.wanda.ccs2.wcf.service.SetEmailInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setEmailInfo operation
           */
            public void receiveErrorsetEmailInfo(java.lang.Exception e) {
            }
                


    }
    