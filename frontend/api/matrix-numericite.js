import BaseApi from './base'
import { API_MATRIX } from '../config';

export default class MatrixNumericiteApi extends BaseApi {

  async register(params) {
    let response = await fetch(`${API_MATRIX}/register`, { method: 'POST', body: JSON.stringify(params) });
    return (await response.json())
  }

  async getProfileMatrix(params) {
    let response = await fetch(`${API_MATRIX}/profile/${params.user_id}`);
    return (await response.json())
  }

  async setDisplayName(params) {
    let response = await fetch(`${API_MATRIX}/profile/${params.user_id}/displayname?access_token=${params.access_token}`, { 
      method: 'PUT', 
      body: JSON.stringify({ displayname: params.display_name }) 
    });
    return (await response.json())
  }

  async setAvatarUrl(params) {
    let response = await fetch(`${API_MATRIX}/profile/${params.user_id}/avatar_url?access_token=${params.access_token}`, { 
      method: 'PUT', 
      body: JSON.stringify({ avatar_url: params.avatar_url }) 
    });
    return (await response.json())
  }

  async login(params) {
    let response = await fetch(`${API_MATRIX}/login`, { method: 'POST', body: JSON.stringify(params) });
    return (await response.json())
  }

  async usernameAvailable(params) {
    let response = await fetch(`${API_MATRIX}/register/available?username=${params.username}`);
    return (await response.json())
  }

  async createRoom(params) {
    let response = await fetch(`${API_MATRIX}/createRoom?access_token=${params.access_token}`, { 
      method: 'POST',
      body: JSON.stringify({})
    });
    return (await response.json())
  }

  async inviteUserToRoom(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/invite?access_token=${params.access_token}`, {
      method: 'POST',
      body: JSON.stringify({ user_id: params.member_user_id })
    });
    return (await response.json())
  }

  async joinRoom(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/join?access_token=${params.access_token}`, {
      method: 'POST',
      body: JSON.stringify({ user_id: params.user_id })
    });
    return (await response.json())
  }

  async retrieveState(params) {
    let response = await fetch(`${API_MATRIX}/sync?access_token=${params.access_token}&timeout=${params.timeout}${params.next_batch ? '&since=' + params.next_batch : '' }`);
    return (await response.json())
  }

  async retrieveMessages(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/messages?access_token=${params.access_token}&from=${params.token ? params.token : 'END'}&filter=${params.filter}&dir=b&limit=${params.limit}`);
    return (await response.json())
  }

  async sendMessage(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/send/m.room.message?access_token=${params.access_token}`, {
      method: 'POST',
      body: JSON.stringify({ msgtype: 'm.text', body: params.message })
    });
    return (await response.json())
  }

  async retrieveMembers(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/members?access_token=${params.access_token}`);
    return (await response.json())
  }

  async retrieveUserPresence(params) {
    let response = await fetch(`${API_MATRIX}/presence/${params.user_id}/status?access_token=${params.access_token}`);
    return (await response.json())
  }

  async userStartedTyping(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/typing/${params.user_id}?access_token=${params.access_token}`, {
      method: 'PUT',
      body: JSON.stringify(params.body)
    })
    return (await response.json())
  }

  async setRoomReadMarkers(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/read_markers?access_token=${params.access_token}`, {
      method: 'POST',
      body: JSON.stringify(params.body)
    })
    return (await response.json())
  }

  async sendReceiptForEventId(params) {
    let response = await fetch(`${API_MATRIX}/rooms/${params.room_id}/receipt/${params.receipt_type}/${params.event_id}?access_token=${params.access_token}`, {
      method: 'POST',
      body: JSON.stringify({})
    })
    return (await response.json())
  }

}
