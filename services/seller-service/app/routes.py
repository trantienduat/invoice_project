from flask import Blueprint, request, jsonify
from app import db
from app.models import Seller
from datetime import datetime

bp = Blueprint('sellers', __name__, url_prefix='/api/sellers')

@bp.route('/', methods=['GET'])
def get_sellers():
    """Get all sellers with optional filtering"""
    company_id = request.args.get('company_id', type=int)
    status = request.args.get('status')
    territory = request.args.get('territory')
    
    query = Seller.query
    
    if company_id:
        query = query.filter_by(company_id=company_id)
    if status:
        query = query.filter_by(status=status)
    if territory:
        query = query.filter_by(territory=territory)
    
    sellers = query.all()
    return jsonify([seller.to_dict() for seller in sellers]), 200

@bp.route('/<int:seller_id>', methods=['GET'])
def get_seller(seller_id):
    """Get a specific seller by ID"""
    seller = Seller.query.get_or_404(seller_id)
    return jsonify(seller.to_dict()), 200

@bp.route('/', methods=['POST'])
def create_seller():
    """Create a new seller"""
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    required_fields = ['name', 'email', 'company_id']
    for field in required_fields:
        if field not in data:
            return jsonify({'error': f'Missing required field: {field}'}), 400
    
    # Check if email already exists
    existing = Seller.query.filter_by(email=data['email']).first()
    if existing:
        return jsonify({'error': 'Seller with this email already exists'}), 409
    
    seller = Seller(
        name=data['name'],
        email=data['email'],
        phone=data.get('phone'),
        company_id=data['company_id'],
        commission_rate=data.get('commission_rate', 0.0),
        territory=data.get('territory'),
        status=data.get('status', 'active')
    )
    
    db.session.add(seller)
    db.session.commit()
    
    return jsonify(seller.to_dict()), 201

@bp.route('/<int:seller_id>', methods=['PUT'])
def update_seller(seller_id):
    """Update an existing seller"""
    seller = Seller.query.get_or_404(seller_id)
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    # Update fields if provided
    if 'name' in data:
        seller.name = data['name']
    if 'phone' in data:
        seller.phone = data['phone']
    if 'commission_rate' in data:
        seller.commission_rate = data['commission_rate']
    if 'territory' in data:
        seller.territory = data['territory']
    if 'status' in data:
        seller.status = data['status']
    
    seller.updated_at = datetime.utcnow()
    db.session.commit()
    
    return jsonify(seller.to_dict()), 200

@bp.route('/<int:seller_id>', methods=['DELETE'])
def delete_seller(seller_id):
    """Delete a seller"""
    seller = Seller.query.get_or_404(seller_id)
    db.session.delete(seller)
    db.session.commit()
    
    return jsonify({'message': 'Seller deleted successfully'}), 200

@bp.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({'status': 'healthy', 'service': 'seller-service'}), 200
