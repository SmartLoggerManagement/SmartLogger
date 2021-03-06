package fr.saagie.smartlogger.db.mysql

import java.io.{BufferedInputStream, BufferedReader}
import java.nio.CharBuffer
import java.sql.{Clob, PreparedStatement, ResultSet, Timestamp}
import java.util.UUID
import javax.sql.rowset.serial.SerialClob

import fr.saagie.smartlogger.db.model.attributes.{Attribute, AttributeFactory}

/**
  * An attribute factory, used to build new attributes
  * for objects stored in MySQL data tables.
  *
  * @author Franck CARON
  * @since SmartLogger 0.2
  * @version 1.0
  */
object AttrMySQLFactory extends AttributeFactory {
  /**
    * Creates a new attribute to store CLOBs (Texts without size limits)
    */
  override def newCLOB(value: String): Attribute[String] = new Attribute[String](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "TEXT"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      // Retrieving
      val reader = new BufferedReader(result.getCharacterStream(columnLabel))
      val text = new StringBuilder

      // Reading text from reader
      val it = reader.lines().iterator()
      while (it.hasNext) {
        text.append(it.next())
      }
      reader.close()

      // Assigning text to the attribute's value
      obj = text.toString
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setClob(parameterIndex, new SerialClob(obj.toCharArray))
    }
  }

  /**
    * Creates a new attribute to store UUIDs (Identifiers strings)
    */
  override def newUUID(value: UUID) = new Attribute[UUID](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "VARCHAR(36)"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = UUID.fromString(result.getString(columnLabel))
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setString(parameterIndex, obj.toString)
    }
  }

  /**
    * Creates a new attribute to store Integers
    */
  override def newInt(value: Int): Attribute[Int] = new Attribute[Int](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "INT"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getInt(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setInt(parameterIndex, obj)
    }
  }

  /**
    * Creates a new attribute to store Doubles
    */
  override def newDouble(value: Double): Attribute[Double] = new Attribute[Double](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "DOUBLE"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getDouble(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setDouble(parameterIndex, obj)
    }
  }

  /**
    * Creates a new attribute to store Strings (n chars max.)
    *
    * @param n The size of the string
    */
  override def newString(value: String, n: Int): Attribute[String] = new Attribute[String](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "VARCHAR(" + n + ')'

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getString(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setString(parameterIndex, obj)
    }
  }

  /**
    * Creates a new attribute to store Dates
    */
  override def newDate(value: Timestamp): Attribute[Timestamp] = new Attribute[Timestamp](value) {
    // REQUESTS
    /**
      * @inheritdoc
      */
    override def getType(): String = "TIMESTAMP"

    /**
      * @inheritdoc
      */
    override def read(result: ResultSet, columnLabel: String): Unit = {
      obj = result.getTimestamp(columnLabel)
    }

    // COMMAND
    /**
      * @inheritdoc
      */
    override def write(state: PreparedStatement, parameterIndex: Int): Unit = {
      state.setTimestamp(parameterIndex, obj)
    }
  }
}
