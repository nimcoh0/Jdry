package org.softauto.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;


/**
 * custom logger wrapper to mange log format . will write all logs using this Marker = JDRY
 * all logs will be write to output according to log4j configuration
 *
 */
public class Logger {
    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public String getTargetName(){
        return LogManager.getLoggerClass(this).getName();
    }

    public void fatal(Marker marker, String message, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.FATAL,marker,message,clazz,t);
    }

    public void fatal(String message, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.FATAL,JDRY,message,clazz,t);
    }

    public void fatal(String message, String t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.FATAL,JDRY,message,clazz,t);
    }

    public void fatal(Marker marker,String message){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.FATAL,marker,message,clazz);
    }

    public void fatal(String message){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.FATAL,JDRY,message,clazz);
    }

    public void info(Marker marker, String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.INFO,marker,msg,clazz);
    }

    public void info(String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.INFO,JDRY,msg,clazz);
    }

    public void info(Marker marker, String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.INFO,marker,msg,clazz,t);
    }

    public void info(String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.INFO,JDRY,msg,clazz,t);
    }

    public void info(String msg, String t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.INFO,JDRY,msg,clazz,t);
    }

    public void debug(Marker marker, String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.DEBUG,marker,msg,clazz);
    }

    public void debug(String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.DEBUG,JDRY,msg,clazz,t);
    }

    public void debug(String msg, String t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.DEBUG,JDRY,msg,clazz,t);
    }

    public void debug(String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.DEBUG,JDRY,msg,clazz);

    }

    public void debug(Marker marker, String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.DEBUG,marker,msg,clazz,t);
    }

    public void error(Marker marker, String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.ERROR,marker,msg,clazz);
    }

    public void error(String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.ERROR,JDRY,msg,clazz);
    }

    public void error(Marker marker, String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.ERROR,marker,msg,clazz,t);
    }

    public void error(String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.ERROR,JDRY,msg,clazz,t);
    }

    public void error(String msg, String t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.ERROR,JDRY,msg,clazz,t);
    }

    public void warn(Marker marker, String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.WARN,marker,msg,clazz);
    }

    public void warn(String msg){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.WARN,JDRY,msg,clazz);
    }

    public void warn(Marker marker, String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.WARN,marker,msg,clazz,t);
    }

    public void warn(String msg, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.WARN,JDRY,msg,clazz,t);
    }

    public void warn(String msg, String t){
        Class clazz = LogManager.getLoggerClass(this);
        log(Level.WARN,JDRY,msg,clazz,t);
    }

    public void log(Level level,Marker marker, String message, Throwable t){
        Class clazz = LogManager.getLoggerClass(this);
        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        if(LogManager.isStatus())
        logger.log(level,JDRY,message,clazz,t);
    }

    public void log(Level level,Marker marker, String message){
        Class clazz = LogManager.getLoggerClass(this);
        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        if(LogManager.isStatus())
        logger.log(level,JDRY,message,clazz);
    }

    public void log(Level level,Marker marker, String message,Class clazz){
        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        if(LogManager.isStatus())
        logger.log(level,marker,message,clazz);
    }

    public void log(Level level,Marker marker, String message,Class clazz,Throwable t){
        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        if(LogManager.isStatus())
        logger.log(level,marker,message,clazz,t);
    }

    public void log(Level level,Marker marker, String message,Class clazz,String t){
        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        if(LogManager.isStatus())
        logger.log(level,marker,message,clazz,t);
    }




}
