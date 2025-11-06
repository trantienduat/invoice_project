from flask import Blueprint, request, jsonify
from app import db
from app.models import Issuer
from datetime import datetime

bp = Blueprint('issuers', __name__, url_prefix='/api/issuers')

@bp.route('/', methods=['GET'])
def get_issuers():
    """Get all issuers with optional filtering"""
    company_id = request.args.get('company_id', type=int)
    status = request.args.get('status')
    
    query = Issuer.query
    
    if company_id:
        query = query.filter_by(company_id=company_id)
    if status:
        query = query.filter_by(status=status)
    
    issuers = query.all()
    return jsonify([issuer.to_dict() for issuer in issuers]), 200

@bp.route('/<int:issuer_id>', methods=['GET'])
def get_issuer(issuer_id):
    """Get a specific issuer by ID"""
    issuer = Issuer.query.get_or_404(issuer_id)
    return jsonify(issuer.to_dict()), 200

@bp.route('/', methods=['POST'])
def create_issuer():
    """Create a new issuer"""
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    required_fields = ['name', 'email', 'company_id']
    for field in required_fields:
        if field not in data:
            return jsonify({'error': f'Missing required field: {field}'}), 400
    
    # Check if email already exists
    existing = Issuer.query.filter_by(email=data['email']).first()
    if existing:
        return jsonify({'error': 'Issuer with this email already exists'}), 409
    
    issuer = Issuer(
        name=data['name'],
        email=data['email'],
        phone=data.get('phone'),
        department=data.get('department'),
        position=data.get('position'),
        company_id=data['company_id'],
        status=data.get('status', 'active')
    )
    
    db.session.add(issuer)
    db.session.commit()
    
    return jsonify(issuer.to_dict()), 201

@bp.route('/<int:issuer_id>', methods=['PUT'])
def update_issuer(issuer_id):
    """Update an existing issuer"""
    issuer = Issuer.query.get_or_404(issuer_id)
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    # Update fields if provided
    if 'name' in data:
        issuer.name = data['name']
    if 'phone' in data:
        issuer.phone = data['phone']
    if 'department' in data:
        issuer.department = data['department']
    if 'position' in data:
        issuer.position = data['position']
    if 'status' in data:
        issuer.status = data['status']
    
    issuer.updated_at = datetime.utcnow()
    db.session.commit()
    
    return jsonify(issuer.to_dict()), 200

@bp.route('/<int:issuer_id>', methods=['DELETE'])
def delete_issuer(issuer_id):
    """Delete an issuer"""
    issuer = Issuer.query.get_or_404(issuer_id)
    db.session.delete(issuer)
    db.session.commit()
    
    return jsonify({'message': 'Issuer deleted successfully'}), 200

@bp.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({'status': 'healthy', 'service': 'issuer-service'}), 200
