/*
 * Copyright (c) 2005, Steve Heath, Henri Yandell
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the 
 * following conditions are met:
 * 
 * + Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * 
 * + Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * 
 * + Neither the name of OSJava nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.osjava.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Destination;

public abstract class MemoryMessageConsumer implements MessageConsumer {

    private MessageListener listener;
    private Destination destination;
    private String messageSelector;
    protected boolean noLocal;

    public MemoryMessageConsumer(Destination destination) {
        this.destination = destination;
    }

    public MemoryMessageConsumer(Destination destination, String messageSelector) {
        this.destination = destination;
        this.messageSelector = messageSelector;
    }

    public MemoryMessageConsumer(Destination destination, String messageSelector, boolean noLocal) {
        this.destination = destination;
        this.messageSelector = messageSelector;
        this.noLocal = noLocal;
    }

    protected Destination getDestination() throws JMSException {
        return this.destination;
    }

    public String getMessageSelector() throws JMSException {
        return this.messageSelector;
    }

    public MessageListener getMessageListener() throws JMSException {
        return this.listener;
    }

    public void setMessageListener(MessageListener listener) throws JMSException {
        this.listener = listener;
    }

    public abstract Message receive() throws JMSException;

    // TODO: Implement timeout?
    public Message receive(long timeout) throws JMSException {
        TimedReceiver receiver = new TimedReceiver();
        receiver.start();
        try {
            receiver.join(timeout);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        receiver.interrupt();
        if (receiver.jmsEx != null)
            throw receiver.jmsEx;
        return receiver.msg;
    }

    // TODO: Implement noWait?
    public Message receiveNoWait() throws JMSException {
        return receive(0);
    }

    public void close() throws JMSException {
        // TODO: Implement this
    }
    class TimedReceiver extends Thread {
        public Message msg = null;
        public JMSException jmsEx = null;
        public TimedReceiver() { super("Temporary receive(timeout) timer"); }
        public void run () {
            try {
                msg = receive();
            } catch (JMSException e) {
                jmsEx = e;
            }
        }
    }
}
