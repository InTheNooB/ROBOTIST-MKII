package wrk;

/**
 *
 * @author dingl01
 */
public enum PacketTypes {

    // [NAME]               [Server Format]                     [Client Format]  
    // -------------------------------------------------------------------------

    // Sequence
    READ_SEQUENCE,      //  [-]                                 [SEQUENCE]
 
    // Robot
    MOVE_ROBOT_BODY,    //                                      [LeftSpeed;RightSpeed]

    // -------------------------------------------------------------------------
}
