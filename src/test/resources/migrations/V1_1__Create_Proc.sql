CREATE PROC schemademo.updateresource(@id UNIQUEIDENTIFIER, @name VARCHAR(400))
AS
  BEGIN
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
    BEGIN TRANSACTION
    	UPDATE schemademo.resource
    	SET name = @name
    	WHERE id = @id
    COMMIT
  END;
GO