CREATE PROC schema_demo.updateresource(@id UNIQUEIDENTIFIER, @name VARCHAR(400))
AS
  BEGIN
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
    BEGIN TRANSACTION
    	UPDATE schema_demo.resource
    	SET name = @name
    	WHERE id = @id
    COMMIT
  END;
GO