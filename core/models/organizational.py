from django.db import models


class GeneralDirection(models.Model):
    """Top-level organizational unit - equivalent to Directiongeneral.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)  # nom
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'directiongeneral'
        ordering = ['name']
    
    def __str__(self):
        return self.name


class Direction(models.Model):
    """Mid-level organizational unit - equivalent to Direction.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)  # nom
    general_direction = models.ForeignKey(
        GeneralDirection, 
        on_delete=models.CASCADE, 
        related_name='directions'
    )
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'direction'
        ordering = ['name']
    
    def __str__(self):
        return self.name


class Department(models.Model):
    """Operational department - equivalent to Departement.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)  # nom
    direction = models.ForeignKey(
        Direction, 
        on_delete=models.CASCADE, 
        related_name='departments'
    )
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'departement'
        ordering = ['name']
    
    def __str__(self):
        return self.name


class Position(models.Model):
    """Job positions - equivalent to Poste.java"""
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=300)  # nom
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'poste'
        ordering = ['name']
    
    def __str__(self):
        return self.name